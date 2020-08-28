package per.jaceding.template.config;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Redis Cache 配置
 *
 * @author jaceding
 * @date 2020/8/28
 */
@EnableCaching
@Configuration
public class RedisCacheConfiguration extends CachingConfigurerSupport {

    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder key = new StringBuilder();
            key.append(target.getClass().getName()).append(":");
            key.append(method.getName());
            if (ArrayUtil.isNotEmpty(params)) {
                key.append(":").append(Arrays.deepToString(params));
            }
            return key.toString();
        };
    }

    @Bean
    public JsonJacksonCodec jsonJacksonCodec(ObjectMapper objectMapper) {
        return new JsonJacksonCodec(objectMapper);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient, JsonJacksonCodec jsonJacksonCodec) {
        RedissonSpringCacheManager redissonSpringCacheManager = new RedissonSpringCacheManager(redissonClient);
        redissonSpringCacheManager.setCodec(jsonJacksonCodec);
        return redissonSpringCacheManager;
    }
}
