package per.jaceding.demo.stampedlock;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jaceding
 * @date 2021/5/24
 */
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class TestAdd {

    public static final int COUNT = 10000;

    public static void main(String[] args) throws RunnerException, InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        Options opt = new OptionsBuilder()
                .include(TestAdd.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void test1() {
        int num = 0;
        for (int i = 0; i < COUNT; i++) {
            num++;
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void test2() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < COUNT; i++) {
            atomicInteger.incrementAndGet();
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void test3() {
        int num = 0;
        for (int i = 0; i < COUNT; i++) {
            synchronized (TestAdd.class) {
                num++;
            }
        }
    }
}
