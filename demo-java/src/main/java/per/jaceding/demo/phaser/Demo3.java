package per.jaceding.demo.phaser;

import java.util.concurrent.*;

/**
 * @author jaceding
 * @date 2021/4/6
 */
public class Demo3 {

    public static void main(String[] args) throws InterruptedException {
        int nCore = Runtime.getRuntime().availableProcessors() * 2;
        int nQueue = 10;
        ExecutorService executorService = new ThreadPoolExecutor(nCore, nCore,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(nQueue),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        Phaser master = new Phaser(1);
        Phaser worker = new Phaser(nQueue);

        for (int i = 0; i < nQueue; i++) {
            executorService.execute(() -> {
                master.awaitAdvance(master.getPhase());
                System.out.println(Thread.currentThread().getName() + " arrive");
                worker.arrive();
            });
        }
        TimeUnit.SECONDS.sleep(1);
        master.arrive();
        worker.awaitAdvance(worker.getPhase());
        System.out.println("main");
        executorService.shutdown();
    }
}
