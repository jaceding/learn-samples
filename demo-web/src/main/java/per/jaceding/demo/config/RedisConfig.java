package per.jaceding.demo.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;

/**
 * Redis 配置类
 *
 * @author jaceding
 * @date 2021/1/20
 */
//@EnableCaching 测试项目关闭缓存
//@Configuration
public class RedisConfig {

    private final ObjectMapper objectMapper;

    public RedisConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy();
        // 必须设置，否则无法将JSON转化为对象，会转化成Map类型
        this.objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public GenericJackson2JsonRedisSerializer redisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory,
                                                   StringRedisSerializer stringRedisSerializer) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        stringRedisTemplate.setKeySerializer(stringRedisSerializer);
        stringRedisTemplate.setValueSerializer(stringRedisSerializer);
        stringRedisTemplate.setHashKeySerializer(stringRedisSerializer);
        stringRedisTemplate.setHashValueSerializer(stringRedisSerializer);
        return stringRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,
                                                       StringRedisSerializer stringRedisSerializer,
                                                       GenericJackson2JsonRedisSerializer redisSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 缓存设置
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()))
                .entryTtl(Duration.ofDays(2));
        return new RedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                redisCacheConfiguration);
    }

    @Bean("defaultKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> target.getClass().getSimpleName()
                + StrUtil.C_UNDERLINE + method.getName()
                + Arrays.toString(params);
    }
}