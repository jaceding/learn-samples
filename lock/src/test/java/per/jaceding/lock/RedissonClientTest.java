package per.jaceding.lock;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 测试RedissonClient
 *
 * @author jaceding
 * @date 2020/8/24
 */
@SpringBootTest(classes = Application.class)
public class RedissonClientTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedisTemplate() {
    }

    @Test
    void testLock() throws InterruptedException {
        RLock lock = redissonClient.getLock("lock1");
        // 如果设置了最大的持锁时间（leaseTime），则守护进程不会续租
//        lock.tryLock();
//        lock.tryLock(10, TimeUnit.SECONDS);
        lock.tryLock(10, 20, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(100 * 100);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    @Test
    void testReadWriteLock() throws InterruptedException {
        RReadWriteLock lock = redissonClient.getReadWriteLock("lock1");
        RLock rLock = lock.readLock();
        RLock wLock = lock.writeLock();
        rLock.tryLock();
        new Thread(() -> {
            RReadWriteLock tlock = redissonClient.getReadWriteLock("lock1");
            RLock trLock = tlock.readLock();
            trLock.tryLock();
            trLock.tryLock();
        }).start();
        wLock.tryLock(20, TimeUnit.SECONDS);
    }

    @Test
    void testFairLock() throws InterruptedException {
        RLock failLock = redissonClient.getFairLock("failLock");
        failLock.tryLock(5, TimeUnit.SECONDS);
        new Thread(() -> {
            RLock lock = redissonClient.getFairLock("failLock");
            try {
                lock.tryLock(20, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        if (failLock.isHeldByCurrentThread()) {
            failLock.unlock();
        }
        System.out.println("");
    }
}
