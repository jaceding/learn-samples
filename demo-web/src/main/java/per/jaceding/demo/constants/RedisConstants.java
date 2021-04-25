package per.jaceding.demo.constants;

/**
 * Redis 相关常量
 *
 * @author jaceding
 * @date 2021/4/10
 */
public class RedisConstants {

    public static final String PREFIX = "wt";

    public static final String SEPARATOR = ":";

    /**
     * 天气缓存
     */
    public static final String WEATHER_CACHE = PREFIX + SEPARATOR + "weather";
}
