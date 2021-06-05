package per.jaceding.demo.disruptor;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import sun.misc.Contended;

import java.util.concurrent.TimeUnit;

/**
 * 测试实现不同缓存行填充的性能差距
 *
 * @author jaceding
 * @date 2021/6/5
 */
@SuppressWarnings("all")
@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class CacheLineTest2 {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CacheLineTest2.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void cacheLine1() throws InterruptedException {
        Padding1 padding = new Padding1();
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                padding.x = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                padding.x = i;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Benchmark
    public void cacheLine2() throws InterruptedException {
        Padding2 padding = new Padding2();
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                padding.x = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                padding.x = i;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Benchmark
    public void cacheLine3() throws InterruptedException {
        Padding3 padding = new Padding3();
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                padding.x = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                padding.x = i;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Benchmark
    public void cacheLine4() throws InterruptedException {
        Padding4 padding = new Padding4();
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                padding.x = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                padding.x = i;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Benchmark
    public void cacheLine5() throws InterruptedException {
        Padding5 padding = new Padding5();
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                padding.x = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                padding.x = i;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static class Padding1 {
        public long p1, p2, p3, p4, p5, p6, p7, p8;
        public volatile long x = 0L;
    }

    static class Padding2 {
        public long p1, p2, p3, p4;
        public volatile long x = 0L;
    }

    static class Padding {
        public long p1, p2, p3, p4, p5, p6, p7, p8;
    }

    static class Padding3 extends Padding {
        public volatile long x = 0L;
    }

    static class Padding4 {
        @Contended
        public volatile long x = 0L;
    }

    @Contended
    static class Padding5 {
        public volatile long x = 0L;
    }
}
