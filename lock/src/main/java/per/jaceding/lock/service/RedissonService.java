package per.jaceding.lock.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import per.jaceding.lock.aop.Lockable;

import java.util.concurrent.TimeUnit;

/**
 * 测试Redisson
 *
 * @author jaceding
 * @date 2020/8/21
 */
@Slf4j
@Service
public class RedissonService {

    private final RedissonClient redissonClient;

    public RedissonService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 剩余商品库存
     */
    private Integer NUM = 10000;

    /**
     * 模拟减库存
     * 测试结果：1000次并发请求减库存，19次尝试加锁失败，也就是只减了981次库存，剩余库存为9019，正常。
     */
    public String test1() throws InterruptedException {
        RLock lock = redissonClient.getLock("good");
        try {
            if (lock.tryLock(2L, TimeUnit.SECONDS)) {
                log.info("加锁成功");
                TimeUnit.MILLISECONDS.sleep(10);
                // 减库存
                NUM--;
                //如果该线程还持有该锁，那么释放该锁。如果该线程不持有该锁，说明该线程的锁已到过期时间，自动释放锁
                if (lock.isHeldByCurrentThread()) {
                    log.info("释放锁");
                    lock.unlock();
                }
                return "减库存成功，NUM=" + NUM;
            }
            log.info("加锁失败");
        } catch (InterruptedException e) {
            throw e;
        }
        throw new RuntimeException("加锁失败");
    }

    @Lockable(keys = {"good", "name"})
    public String test2(String name) {
        // 减库存
        NUM--;
        return "减库存成功，NUM=" + NUM;
    }
}
