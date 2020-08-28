package per.jaceding.lock.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis(SET KEY VALUE PX NX) 的简易版分布式锁
 *
 * @author jaceding
 * @date 2020/8/21
 */
@Slf4j
@Component
public class SimpleLock {

    /**
     * 默认持锁时间
     */
    private static final int DEFAULT_LEASE_TIME = 20;

    /**
     * 释放锁的脚本
     */
    private static final String UNLOCK_SCRIPT_STRING =
            "if (redis.call('get', KEYS[1]) == ARGV[1])\n" +
                    "then\n" +
                    "    return redis.call('del', KEYS[1])\n" +
                    "else\n" +
                    "    return 0\n" +
                    "end";

    /**
     * 释放锁的脚本
     */
    private static final RedisScript<Integer> UNLOCK_SCRIPT = RedisScript.of(UNLOCK_SCRIPT_STRING, Integer.class);

    /**
     * UUID
     */
    private static final String UUID = java.util.UUID.randomUUID().toString();

    private static SimpleLock simpleLock;

    private final RedisTemplate<String, String> redisTemplate;

    public SimpleLock(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        simpleLock = this;
    }

    /**
     * 加锁
     *
     * @param key 资源名
     */
    public static void lock(String key) {
        lock(key, DEFAULT_LEASE_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 加锁
     *
     * @param key       资源名
     * @param leaseTime 最大持有时间
     * @param unit      时间单位
     */
    public static void lock(String key, long leaseTime, TimeUnit unit) {
        while (tryAcquire(key, leaseTime, unit)) {
        }
    }

    /**
     * 尝试加锁
     *
     * @param key 资源名
     * @return 是否加锁成功
     */
    public static boolean tryLock(String key) {
        return tryLock(key, -1, DEFAULT_LEASE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 尝试加锁
     *
     * @param key      资源名
     * @param waitTime 最大等待时间
     * @param unit     时间单位（最大等待时间、最大持有时间）
     * @return 是否加锁成功
     */
    public static boolean tryLock(String key, long waitTime, TimeUnit unit) {
        return tryLock(key, waitTime, DEFAULT_LEASE_TIME, unit);
    }

    /**
     * 尝试加锁
     *
     * @param key       资源名
     * @param waitTime  最大等待时间
     * @param leaseTime 最大持有时间
     * @param unit      时间单位（最大等待时间、最大持有时间）
     * @return 是否加锁成功
     */
    public static boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) {
        long time = unit.toMillis(waitTime);
        long currentTime = System.currentTimeMillis();
        do {
            if (tryAcquire(key, leaseTime, unit)) {
                return true;
            }
            Thread.yield();
        } while (System.currentTimeMillis() - currentTime < time);
        return false;
    }

    /**
     * 尝试加锁 [Redis set key value PX NX]
     *
     * @param key       资源名
     * @param leaseTime 最大持有时间
     * @param unit      时间单位（最大等待时间、最大持有时间）
     * @return 是否加锁成功
     */
    private static boolean tryAcquire(String key, long leaseTime, TimeUnit unit) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, getValue(), leaseTime, unit);
    }

    /**
     * 释放锁
     *
     * @param key 资源名
     */
    public static void unlock(String key) {
        // 需要保证释放的是自己持有的锁，而判断是否自己持有+释放锁没有原生的原子命令，这里借助lua脚本完成
        Object result = getRedisTemplate().execute(UNLOCK_SCRIPT, Collections.singletonList(key), getValue());
        log.info("unlock result = " + result);
    }

    private static String getValue() {
        return UUID + Thread.currentThread().getId();
    }

    private static RedisTemplate<String, String> getRedisTemplate() {
        return simpleLock.redisTemplate;
    }
}
