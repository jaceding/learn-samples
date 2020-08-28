package per.jaceding.template.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 测试定时任务
 *
 * @author jaceding
 * @date 2020/8/25
 */
@Slf4j
@Component
public class TestSchedule {

    /**
     * 定时任务需要加上@Async才能实现异步
     */
    @Async
    @Scheduled(cron = "0/10 * * * * ?")
    public void test1() throws InterruptedException {
        log.info("test1 start");
        TimeUnit.SECONDS.sleep(20);
        log.info("test1 end");
    }

    @Scheduled(cron = "0/15 * * * * ?")
    public void test2() throws InterruptedException {
        log.info("test2 start");
        TimeUnit.SECONDS.sleep(20);
        log.info("test2 end");
    }

    @Async
    @Scheduled(cron = "0/8 * * * * ?")
    public void test3() throws InterruptedException {
        log.info("test3 start");
        TimeUnit.SECONDS.sleep(20);
        log.info("test3 end");
    }
}
