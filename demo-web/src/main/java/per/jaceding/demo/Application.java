package per.jaceding.demo;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 主类
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Slf4j
@Import(value = {
        SpringUtil.class
})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info("Started: {}", environment.getProperty("knife4j.swagger.url"));
    }
}
