package per.jaceding.demo.disruptor;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 测试缓存行对性能的影响
 *
 * @author jaceding
 * @date 2020/4/7
 */
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class CacheLineTest {

    static int ARR_SIZE_1 = 1024 * 1024;

    static int ARR_SIZE_2 = 8;

    static long[][] ARR = new long[ARR_SIZE_1][ARR_SIZE_2];

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CacheLineTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void cacheLine() {
        long n;
        for (int i = 0; i < ARR_SIZE_1; i++) {
            for (int j = 0; j < ARR_SIZE_2; j++) {
                n = ARR[i][j];
            }
        }
    }

    @Benchmark
    public void noCacheLine() {
        long n;
        for (int i = 0; i < ARR_SIZE_2; i++) {
            for (int j = 0; j < ARR_SIZE_1; j++) {
                n = ARR[j][i];
            }
        }
    }
}
