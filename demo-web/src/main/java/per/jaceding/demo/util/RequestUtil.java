package per.jaceding.demo.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request 工具类
 *
 * @author jaceding
 * @date 2021/4/9
 */
public class RequestUtil {

    /**
     * 本机IPV6地址
     */
    public static final String LOCAL_HOST_IPV6 = "0:0:0:0:0:0:0:1";

    /**
     * 本机IPV4地址
     */
    public static final String LOCAL_HOST_IPV4 = "127.0.0.1";

    /**
     * 根据请求上下文获取客户端IP
     *
     * @param request 请求上下文
     * @return 客户端IP
     */
    public static String getIp(HttpServletRequest request) {
        String ip = ServletUtil.getClientIP(request);
        if (LOCAL_HOST_IPV6.equals(ip)) {
            return LOCAL_HOST_IPV4;
        }
        return ip;
    }

    /**
     * 获取请求参数
     *
     * @param method 方法
     * @param args   传入参数
     * @return 请求参数
     */
    public static Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            // 将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            // 将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>(16, 0.75f);
                String key = parameters[i].getName();
                if (!StrUtil.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}
