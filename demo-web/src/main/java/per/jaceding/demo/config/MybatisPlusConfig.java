package per.jaceding.demo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

/**
 * Mybatis Plus 配置类
 *
 * @author jaceding
 * @date 2021/1/8
 */
@MapperScan("per.jaceding.*.mapper")
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
public class MybatisPlusConfig {
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_TIME = "updateTime";

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

    /**
     * 乐观锁插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * 自动填充功能
     */
    @Component
    static class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            this.setFieldValByName(CREATE_TIME, LocalDateTime.now(), metaObject);
            this.setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            this.setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
        }
    }
}
