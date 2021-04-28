package per.jaceding.demo.stampedlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.StampedLock;

/**
 * @author jaceding
 * @date 2021/4/2
 */
public class Demo {

    public static void main(String[] args) throws InterruptedException {
        int nCore = Runtime.getRuntime().availableProcessors() * 2;
        int nQueue = 100;
        ExecutorService executorService = new ThreadPoolExecutor(nCore, nCore,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(nQueue),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        MyList<Integer> myList = new MyList<>();
        final CountDownLatch cdMaster = new CountDownLatch(1);
        final CountDownLatch cdWorker = new CountDownLatch(nCore);

        for (int i = 0; i < nCore; i++) {
            executorService.execute(() -> {
                try {
                    cdMaster.await();
                    myList.add(1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cdWorker.countDown();
                }
            });
        }
        cdMaster.countDown();
        cdWorker.await();
        System.out.println("nCore: " + nCore);
        System.out.println("size: " + myList.size());
        executorService.shutdown();
    }

    private static class MyList<T> {
        private final List<T> list = new ArrayList<>();

        private final StampedLock lock = new StampedLock();

        public MyList() {
        }

        public int size() {
            long stamp = lock.tryOptimisticRead();
            int size = list.size();
            if (!lock.validate(stamp)) {
                try {
                    stamp = lock.readLock();
                    size = list.size();
                } finally {
                    lock.unlock(stamp);
                }
            }
            return size;
        }

        public void add(T o) {
            long stamp = lock.writeLock();
            System.out.println("writeLock stamp = " + stamp);
            try {
                list.add(o);
            } finally {
                lock.unlock(stamp);
            }
        }
    }
}
