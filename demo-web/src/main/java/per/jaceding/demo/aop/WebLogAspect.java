package per.jaceding.demo.aop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import per.jaceding.demo.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 日志记录AOP实现
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class WebLogAspect {

    /**
     * Thread Local 请求日志
     */
    private static final ThreadLocal<WebLog> LOGS = new ThreadLocal<>();

    @Value("${system.web-log.enable:false}")
    private Boolean webLogEnable;

    /**
     * 获取WebLog
     *
     * @return WebLog
     */
    @SuppressWarnings("unused")
    public static WebLog getWebLog() {
        return LOGS.get();
    }

    @Pointcut(value = "execution(* per.jaceding.*..controller..*.*(..))")
    private void controllerPointCut() {
    }

    @Pointcut(value = "execution(* per.jaceding.*..exception..*.*(..))")
    private void exceptionPointCut() {
    }

    /**
     * 由于不同的aop版本导致执行顺序发生变化，所以尽量都放入doAround中处理
     */
    @SuppressWarnings("rawtypes")
    @Around("controllerPointCut() || exceptionPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        WebLog webLog = LOGS.get();
        if (Objects.isNull(webLog)) {
            // 获取request
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            assert sra != null;
            HttpServletRequest request = sra.getRequest();

            // 获取method
            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            // 构建WebLog
            webLog = WebLog.builder()
                    .startTime(System.currentTimeMillis())
                    .uri(request.getRequestURI())
                    .url(request.getRequestURL().toString())
                    .method(request.getMethod())
                    .clientIp(RequestUtil.getIp(request))
                    .parameter(RequestUtil.getParameter(method, pjp.getArgs()))
                    .build();
            LOGS.set(webLog);
        }
        // 执行方法
        Object object = pjp.proceed();
        long processMs = System.currentTimeMillis() - webLog.getStartTime();
        if (object instanceof ResponseEntity) {
            webLog.setStatus(((ResponseEntity) object).getStatusCodeValue());
        } else {
            webLog.setStatus(HttpStatus.OK.value());
        }
        webLog.setResult(object);
        webLog.setDuration(processMs);
        // 防止内存泄漏
        LOGS.remove();
        if (webLogEnable) {
            printWebLog(webLog);
        }
        return object;
    }

    /**
     * 打印日志
     *
     * @param webLog 日志内容
     */
    private void printWebLog(WebLog webLog) {
        log.info(webLog.toString());
    }

    /**
     * 请求日志
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WebLog {

        /**
         * URI
         */
        private String uri;

        /**
         * URL
         */
        private String url;

        /**
         * 请求类型
         */
        private String method;

        /**
         * 客户端IP
         */
        private String clientIp;

        /**
         * 请求参数
         */
        private Object parameter;

        /**
         * 响应状态
         */
        private Integer status;

        /**
         * 响应结果
         */
        private Object result;

        /**
         * 开始时间，时间戳，单位毫秒
         */
        private long startTime;

        /**
         * 耗时，单位毫秒
         */
        private Long duration;
    }
}
