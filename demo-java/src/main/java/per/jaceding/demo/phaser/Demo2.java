package per.jaceding.demo.phaser;

import java.util.concurrent.*;

/**
 * @author jaceding
 * @date 2021/4/6
 */
public class Demo2 {

    public static void main(String[] args) throws InterruptedException {
        int nCore = Runtime.getRuntime().availableProcessors() * 2;
        int nQueue = 4;
        ExecutorService executorService = new ThreadPoolExecutor(nCore, nCore,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(nQueue),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        Phaser phaser = new Phaser(nQueue);

        for (int i = 0; i < nQueue; i++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " 到达");
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() + " 笔试");
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() + " 面试");
            });
        }
        TimeUnit.SECONDS.sleep(1);
        executorService.shutdown();
    }
}
