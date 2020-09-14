package per.jaceding.template.util;

import com.sun.deploy.resources.ResourceManager;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Yml 配置工具类
 *
 * @author jaceding
 * @date 2020/9/14
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
public class YmlUtils {

    private static final String CONFIG_FILE = "application.yml";

    private static Map<String, Object> properties;

    private static synchronized void loadProperties() {
        if (properties == null) {
            InputStream inputStream = ResourceManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            Yaml yaml = new Yaml();
            properties = yaml.loadAs(inputStream, Map.class);
        }
    }

    /**
     * 获取配置，并指定返回的类型
     *
     * @param key   配置的key
     * @param clazz 未使用
     * @param <T>   返回类型
     * @return 配置的值
     */
    public static <T> T getValue(String key, Class<T> clazz) {
        return (T) getValueByKey(key);
    }

    /**
     * 获取配置的值
     *
     * @param key 配置的key
     * @return 配置的值
     */
    public static String getValue(String key) {
        return (String) getValueByKey(key);
    }

    /**
     * 获取配置的值
     *
     * @param key 配置名
     * @return 配置的值
     */
    public static Object getValueByKey(String key) {
        return getValueByKey(key, null);
    }

    /**
     * 获取配置的值， 可指定默认值
     *
     * @param key          配置的key
     * @param defaultValue 默认值
     * @param <T>          返回类型
     * @return 配置的值
     */
    public static <T> T getValueByKey(String key, T defaultValue) {
        if (properties == null) {
            loadProperties();
        }
        String separator = ".";
        String[] separatorKeys;
        if (key.contains(separator)) {
            // 取下面配置项的情况, user.path.keys 这种
            separatorKeys = key.split("\\.");
        } else {
            // 直接取一个配置项的情况, user
            Object res = properties.get(key);
            return res == null ? defaultValue : (T) res;
        }
        // 下面肯定是取多个的情况
        String finalValue = null;
        Object tempObject = properties;
        for (int i = 0; i < separatorKeys.length; i++) {
            //如果是user[0].path这种情况,则按list处理
            String innerKey = separatorKeys[i];
            Integer index = null;
            if (innerKey.contains("[")) {
                // 如果是user[0]的形式,则index = 0 , innerKey=user
                index = getIndex(innerKey);
                innerKey = innerKey.substring(0, innerKey.indexOf("["));
            }
            Map<String, Object> mapTempObj = (Map) tempObject;
            Object object = mapTempObj.get(innerKey);
            // 如果没有对应的配置项,则返回设置的默认值
            if (object == null) {
                return defaultValue;
            }
            Object targetObj = object;
            if (index != null) {
                // 如果是取的数组中的值,在这里取值
                targetObj = ((ArrayList) object).get(index);
            }
            // 一次获取结束,继续获取后面的
            tempObject = targetObj;
            if (i == separatorKeys.length - 1) {
                //循环结束
                return (T) targetObj;
            }
        }
        return null;
    }

    /**
     * 解析配置的key中的索引值
     *
     * @param key 配置的key
     * @return 索引值
     */
    private static int getIndex(String key) {
        int startIndex = key.indexOf("[");
        int endIndex = key.indexOf("]");
        return Integer.parseInt(key.substring(startIndex + 1, endIndex));
    }
}
