package per.jaceding.lock.aop;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 日志记录AOP实现
 *
 * @author jaceding
 * @date 2020/7/24
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 请求地址
     */
    private final ThreadLocal<String> url = new ThreadLocal<>();
    /**
     * 请求类型
     */
    private final ThreadLocal<String> type = new ThreadLocal<>();
    /**
     * 请求ip
     */
    private final ThreadLocal<String> clientIp = new ThreadLocal<>();
    /**
     * Token信息
     */
    private final ThreadLocal<String> token = new ThreadLocal<>();
    /**
     * 请求参数
     */
    private final ThreadLocal<Object> params = new ThreadLocal<>();
    /**
     * 返回结果
     */
    private final ThreadLocal<Object> result = new ThreadLocal<>();
    /**
     * 方法开始执行时间
     */
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    /**
     * 方法执行时间
     */
    private final ThreadLocal<Long> executeTime = new ThreadLocal<>();

    /**
     * 获取用户ip地址
     *
     * @param request 请求对象
     * @return ip
     */
    @SuppressWarnings("all")
    private static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 获取请求参数
     *
     * @param request 请求对象
     * @return 请求体
     */
    private static Object getParams(HttpServletRequest request) {
        if (RequestMethod.GET.name().equals(request.getMethod())) {
            return getRequestParameter(request);
        } else if (ContentType.APPLICATION_FORM_URLENCODED.getMimeType().equals(request.getContentType())) {
            return getRequestParameter(request);
        } else {
            return getRequestInputStream(request);
        }
    }

    /**
     * 获取RequestParameter参数
     *
     * @param request 请求对象
     * @return 请求参数
     */
    public static Map<String, String> getRequestParameter(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>(8, 0.75f);
        try {
            Enumeration<String> parameterNames = request.getParameterNames();
            if (parameterNames != null) {
                while (parameterNames.hasMoreElements()) {
                    String parameterName = parameterNames.nextElement();
                    parameters.put(parameterName, request.getParameter(parameterName));
                }
            }
        } catch (Exception ignored) {
        }
        return parameters;
    }

    /**
     * 获取 RequestInputStream参数
     *
     * @param request 请求对象
     * @return 请求参数
     */
    public static String getRequestInputStream(HttpServletRequest request) {
        try {
            byte[] requestBody = StreamUtils.copyToByteArray(request.getInputStream());
            return new String(requestBody, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
        return "";
    }

    @Pointcut(value = "execution(* per.jaceding.*.controller..*.*(..))")
    private void controllerPointCut() {
    }

    @Pointcut(value = "execution(* per.jaceding.*.api..*.*(..))")
    private void apiPointCut() {
    }

    @Pointcut(value = "execution(* per.jaceding.*.exception.GlobalDefaultExceptionHandler.*(..))")
    private void exceptionHandlerPointCut() {
    }

    @Before("controllerPointCut() || apiPointCut()")
    public void doBefore() {
        // 记录方法开始执行的时间
        startTime.set(System.currentTimeMillis());
    }

    @Around("controllerPointCut() || apiPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 获取request
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();

        // 获取请求地址,请求类型,ip,token,请求体,返回结果,方法开始执行时间
        url.set(request.getRequestURI());
        type.set(request.getMethod());
        clientIp.set(getIpAddr(request));
        token.set(request.getHeader("Authorization"));
        params.set(getParams(request));
        // 执行方法
        Object object = pjp.proceed();
        // 保存执行结果
        result.set(object == null ? "" : object);
        return object;
    }

    @After("controllerPointCut() || apiPointCut()")
    public void doAfter() {
        // 获取方法执行时间
        long processMs = System.currentTimeMillis() - startTime.get();
        executeTime.set(processMs);
        // 如果结果不为空，则打印日志及清除数据。否则说明接口抛出异常，由异常处理器返回结果。
        if (result.get() != null) {
            // 打印日志
            printLog();
            // 清除数据
            clear();
        }
    }

    @Before("exceptionHandlerPointCut()")
    public void doBeforeExceptionHandler() {
        // 获取request
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();

        // 记录开始执行的时间，请求地址,请求类型,ip,token,请求体,返回结果,方法开始执行时间
        startTime.set(System.currentTimeMillis());
        url.set(request.getRequestURI());
        type.set(request.getMethod());
        clientIp.set(getIpAddr(request));
        token.set(request.getHeader("Authorization"));
        params.set(getParams(request));
    }

    @Around("exceptionHandlerPointCut()")
    public Object doAroundExceptionHandler(ProceedingJoinPoint pjp) throws Throwable {
        // 执行方法，获取异常处理器返回结果
        Object object = pjp.proceed();
        if (executeTime.get() == null) {
            // 获取方法执行时间
            long processMs = System.currentTimeMillis() - startTime.get();
            executeTime.set(processMs);
        }
        result.set(object == null ? "" : object);
        // 打印日志
        printLog();
        // 清除数据
        clear();
        return object;
    }

    /**
     * 清除 ThreadLocal，防止内存泄漏
     */
    private void clear() {
        url.remove();
        type.remove();
        clientIp.remove();
        token.remove();
        params.remove();
        result.remove();
        startTime.remove();
        executeTime.remove();
    }

    /**
     * 打印日志
     */
    private void printLog() {
        Map<String, Object> logMap = new LinkedHashMap<>(8, 1f);
        logMap.put("url", url.get());
        logMap.put("type", type.get());
        logMap.put("clientIp", clientIp.get());
        logMap.put("token", token.get());
        logMap.put("params", params.get());
        logMap.put("result", result.get());
        logMap.put("startTime", startTime.get());
        logMap.put("executeTime", executeTime.get());
        log.info(JSONUtil.toJsonStr(logMap));
    }
}