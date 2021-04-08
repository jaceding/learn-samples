package per.jaceding.demo.stampedlock;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

/**
 * @author jaceding
 * @date 2021/4/6
 */
public class StampedLockTest {

    public static void main(String[] args) throws InterruptedException {
        final StampedLock lock = new StampedLock();
        Thread t1 = new Thread(() -> {
            // 获取写锁
            lock.writeLock();
            // 模拟程序阻塞等待其他资源
            LockSupport.park();
        });
        t1.start();
        // 保证t1获取写锁
        Thread.sleep(100);
        Thread t2 = new Thread(() -> {
            // 阻塞在悲观读锁
            lock.readLock();
        });
        t2.start();
        // 保证t2阻塞在读锁
        Thread.sleep(100);
        // 中断线程t2,会导致线程t2所在CPU飙升
        t2.interrupt();
        t2.join();
    }
}
