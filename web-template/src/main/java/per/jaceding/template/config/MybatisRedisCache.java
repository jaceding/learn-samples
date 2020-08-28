package per.jaceding.template.config;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.ibatis.cache.Cache;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Mybatis二级缓存实现
 * <p>
 * 连表查询注意不要使用二级缓存，使用如下
 * <pre> {@code
 * @CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class, properties = {
 *         @Property(name = "size", value = "1024")
 * })
 * }</pre>
 *
 * @author jaceding
 * @date 2020/8/25
 */
@SuppressWarnings("unused")
public class MybatisRedisCache implements Cache {

    private final String id;
    private RMapCache<Object, Object> mapCache;
    private long timeToLive;
    private long maxIdleTime;
    private int maxSize;

    public MybatisRedisCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        getMapCache().put(key, value, timeToLive, TimeUnit.MILLISECONDS, maxIdleTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public Object getObject(Object key) {
        return getMapCache().get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return getMapCache().remove(key);
    }

    @Override
    public void clear() {
        getMapCache().clear();
    }

    @Override
    public int getSize() {
        return getMapCache().size();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    private RMapCache<Object, Object> getMapCache() {
        if (mapCache == null) {
            synchronized (this) {
                RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
                mapCache = redissonClient.getMapCache(id, SpringUtil.getBean(JsonJacksonCodec.class));
                if (maxSize > 0) {
                    mapCache.setMaxSize(maxSize);
                }
            }
        }
        return mapCache;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void setMaxIdleTime(long maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
