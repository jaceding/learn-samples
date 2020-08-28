package per.jaceding.lock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 定时任务 配置类
 * 基于注解@Scheduled默认为单线程，这里修改为使用线程池
 *
 * @author jaceding
 * @date 2020/7/21
 */
@Async
@EnableScheduling
@Configuration
public class ScheduleConfiguration implements SchedulingConfigurer {

    /**
     * 默认核心线程数
     */
    public static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(new ScheduledThreadPoolExecutor(
                DEFAULT_POOL_SIZE,
                new CustomizableThreadFactory()));
    }
}
