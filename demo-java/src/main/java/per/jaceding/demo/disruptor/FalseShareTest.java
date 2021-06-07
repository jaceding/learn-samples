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
 * <p>
 * -XX:-RestrictContended 开启@Contended
 * <p>
 * Benchmark                    Mode  Cnt    Score    Error   Units
 * FalseShareTest.cacheLine1   thrpt   10    0.020 ±  0.001  ops/ms
 * FalseShareTest.cacheLine2   thrpt   10    0.007 ±  0.001  ops/ms
 * FalseShareTest.cacheLine4   thrpt   10    0.021 ±  0.001  ops/ms
 * FalseShareTest.cacheLine5   thrpt   10    0.005 ±  0.001  ops/ms
 * FalseShareTest.noCacheLine  thrpt   10    0.006 ±  0.001  ops/ms
 * FalseShareTest.cacheLine1    avgt   10   49.332 ±  2.496   ms/op
 * FalseShareTest.cacheLine2    avgt   10  136.192 ± 12.444   ms/op
 * FalseShareTest.cacheLine4    avgt   10   49.102 ±  1.846   ms/op
 * FalseShareTest.cacheLine5    avgt   10  209.414 ±  9.778   ms/op
 * FalseShareTest.noCacheLine   avgt   10  176.505 ± 15.624   ms/op
 *
 * @author jaceding
 * @date 2020/4/8
 */
@SuppressWarnings("all")
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
public class FalseShareTest {

    public static final long LOOP = 1000_0000L;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FalseShareTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void noCacheLine() throws InterruptedException {
        NoPadding padding = new NoPadding();
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < LOOP; i++) {
                padding.x1 = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < LOOP; i++) {
                padding.x2 = i;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Benchmark
    public void cacheLine1() throws InterruptedException {
        Padding1 padding = new Padding1();
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < LOOP; i++) {
                padding.x1 = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < LOOP; i++) {
                padding.x2 = i;
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
            for (long i = 0; i < LOOP; i++) {
                padding.x1 = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < LOOP; i++) {
                padding.x2 = i;
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
            for (long i = 0; i < LOOP; i++) {
                padding.x1 = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < LOOP; i++) {
                padding.x2 = i;
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
            for (long i = 0; i < LOOP; i++) {
                padding.x1 = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < LOOP; i++) {
                padding.x2 = i;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static class NoPadding {
        public volatile long x1 = 0L;
        public volatile long x2 = 0L;
    }

    static class Padding1 {
        public long p1, p2, p3, p4, p5, p6, p7, p8;
        public volatile long x1 = 0L;
        public long p11, p12, p13, p14, p15, p16, p17, p18;
        public volatile long x2 = 0L;
        public long p21, p22, p23, p24, p25, p26, p27, p28;
    }

    static class Padding2 {
        public long p1, p2, p3, p4;
        public volatile long x1 = 0L;
        public long p11, p12, p13, p14;
        public volatile long x2 = 0L;
        public long p21, p22, p23, p24;
    }

    static class Padding4 {
        @Contended
        public volatile long x1 = 0L;
        @Contended
        public volatile long x2 = 0L;
    }

    @Contended
    static class Padding5 {
        public volatile long x1 = 0L;
        public volatile long x2 = 0L;
    }
}
