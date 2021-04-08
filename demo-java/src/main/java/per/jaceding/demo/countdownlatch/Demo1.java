package per.jaceding.demo.countdownlatch;

import java.util.concurrent.*;

/**
 * @author jaceding
 * @date 2021/4/6
 */
public class Demo1 {

    public static void main(String[] args) throws InterruptedException {
        int nCore = Runtime.getRuntime().availableProcessors() * 2;
        int nQueue = 10;
        ExecutorService executorService = new ThreadPoolExecutor(nCore, nCore,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(nQueue),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        CountDownLatch master = new CountDownLatch(1);
        CountDownLatch worker = new CountDownLatch(nQueue);
        for (int i = 0; i < nQueue; i++) {
            executorService.execute(() -> {
                try {
                    master.await();
                    System.out.println(Thread.currentThread().getName() + " arrive");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    worker.countDown();
                }
            });
        }
        master.countDown();
        worker.await();
        System.out.println("main");
        executorService.shutdown();
    }
}
