package per.jaceding.demo.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author jaceding
 * @date 2021/4/7
 */
public class Demo2 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            LockSupport.park();
            System.out.println("hello");
        });
        thread.start();
        TimeUnit.SECONDS.sleep(2);
        thread.interrupt();
    }
}
