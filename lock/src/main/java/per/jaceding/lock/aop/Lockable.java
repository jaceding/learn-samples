package per.jaceding.lock.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 自定义注解实现分布式锁
 *
 * @author jaceding
 * @date 2020/8/25
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Lockable {

    String[] keys() default {};

    /**
     * 超时时间
     */
    long waitTime() default Long.MAX_VALUE;

    /**
     * 持锁时间
     */
    long leaseTime() default Long.MAX_VALUE;

    /**
     * 时间单位
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
