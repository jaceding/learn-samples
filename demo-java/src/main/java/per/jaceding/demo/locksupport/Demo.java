package per.jaceding.demo.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author jaceding
 * @date 2021/4/6
 */
public class Demo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LockSupport.park();
        });
        thread.start();
        LockSupport.unpark(thread);
    }
}
