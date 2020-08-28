package per.jaceding.template.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定时任务 配置类
 * 基于注解@Scheduled默认为单线程，这里修改为使用线程池
 *
 * @author jaceding
 * @date 2020/7/21
 */
@EnableAsync
@EnableScheduling
@Configuration
public class ScheduleConfiguration implements SchedulingConfigurer, AsyncConfigurer {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 默认核心线程数
     */
    public static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(DEFAULT_POOL_SIZE);
        scheduler.setAwaitTerminationSeconds(600);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setThreadFactory(new DefaultThreadFactory(applicationName + "-schedule-"));
        scheduler.initialize();
        taskRegistrar.setScheduler(scheduler);
    }

    @Override
    public Executor getAsyncExecutor() {
        return asyncExecutor();
    }

    @Bean
    public ExecutorService asyncExecutor() {
        return new ThreadPoolExecutor(
                DEFAULT_POOL_SIZE,
                DEFAULT_POOL_SIZE,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new DefaultThreadFactory(applicationName + "-async-"),
                new ThreadPoolExecutor.AbortPolicy());
    }

    static class DefaultThreadFactory implements ThreadFactory {

        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = name + POOL_NUMBER.getAndIncrement() + "-";
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
