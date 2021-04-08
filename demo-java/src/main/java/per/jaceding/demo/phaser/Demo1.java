package per.jaceding.demo.phaser;

import java.util.concurrent.*;

/**
 * 1个主线程要等10个worker线程完成之后
 *
 * @author jaceding
 * @date 2021/4/6
 */
public class Demo1 {

    public static void main(String[] args) {
        int nCore = Runtime.getRuntime().availableProcessors() * 2;
        int nQueue = 10;
        ExecutorService executorService = new ThreadPoolExecutor(nCore, nCore,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(nQueue),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        Phaser phaser = new Phaser(nQueue);

        for (int i = 0; i < nQueue; i++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " arrive");
                phaser.arrive();
            });
        }
        phaser.awaitAdvance(phaser.getPhase());
        System.out.println("main");
        executorService.shutdown();
    }
}
