package per.jaceding.demo.stampedlock;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock、ReentrantReadWriteLock
 *
 * @author jaceding
 * @date 2021/4/6
 */
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class BenchmarkTest {

    public static final int READ_NUM = 100000;

    public static final int WRITE_NUM = 10;

    public static final Object OBJ = new Object();

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void stampedLock() {
        List<Object> list = new ArrayList<>(WRITE_NUM * 2);
        new Thread(() -> {
            StampedLock lock = new StampedLock();
            for (int i = 0; i < READ_NUM; i++) {
                long stamp = lock.tryOptimisticRead();
                int size = list.size();
                if (!lock.validate(stamp)) {
                    try {
                        stamp = lock.readLock();
                        size = list.size();
                    } finally {
                        lock.tryUnlockRead();
                    }
                }
            }
        }).start();
        new Thread(() -> {
            StampedLock lock = new StampedLock();
            for (int i = 0; i < WRITE_NUM; i++) {
                long stamp = lock.writeLock();
                try {
                    list.add(OBJ);
                } finally {
                    lock.unlock(stamp);
                }
            }
        }).start();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void reentrantReadWriteLock() {
        List<Object> list = new ArrayList<>(WRITE_NUM * 2);
        new Thread(() -> {
            ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
            for (int i = 0; i < READ_NUM; i++) {
                lock.readLock().lock();
                int size = list.size();
                lock.readLock().unlock();
            }
        }).start();
        new Thread(() -> {
            ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
            for (int i = 0; i < WRITE_NUM; i++) {
                lock.writeLock().lock();
                list.add(OBJ);
                lock.writeLock().unlock();
            }
        }).start();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void sync() {
        List<Object> list = new ArrayList<>(WRITE_NUM * 2);
        new Thread(() -> {
            for (int i = 0; i < READ_NUM; i++) {
                synchronized (list) {
                    int size = list.size();
                }
            }
        });
        new Thread(() -> {
            for (int i = 0; i < WRITE_NUM; i++) {
                synchronized (list) {
                    list.add(OBJ);
                }
            }
        });
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
