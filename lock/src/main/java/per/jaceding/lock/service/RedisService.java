package per.jaceding.lock.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import per.jaceding.lock.util.SimpleLock;

import java.util.concurrent.TimeUnit;

/**
 * 简易版 分布式锁测试
 *
 * @author jaceding
 * @date 2020/8/21
 */
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 剩余商品库存，模拟减库存操作
     */
    private Integer NUM = 10000;

    public String test1() {
        String key = "good";
        if (SimpleLock.tryLock(key, 10, TimeUnit.SECONDS)) {
            NUM--;
            SimpleLock.unlock(key);
            return getClass().getName() + NUM;
        }
        throw new RuntimeException("加锁失败");
    }
}
