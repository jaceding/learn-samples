package per.jaceding.demo.service;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import per.jaceding.demo.util.BloomFilterHelper;

/**
 * 测试布隆过滤器
 *
 * @author jaceding
 * @date 2020/6/18
 */
@Slf4j
@Service
public class TestService {

    /**
     * 用户名-布隆过滤器 key的名称
     */
    private static final String NAME_BLOOM_FILTER_KEY = "bf:name";

    /**
     * 用户名-布隆过滤器
     */
    private static final BloomFilterHelper<CharSequence> NAME_BLOOM_FILTER_HELPER =
            new BloomFilterHelper<>(
                    Funnels.stringFunnel(Charsets.UTF_8),
                    Integer.MAX_VALUE,
                    0.001);

    private final RedisService redisService;

    public TestService(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 添加用户名
     *
     * @param name 用户名
     */
    public void addName(String name) {
        log.info("添加用户：{}", name);
        redisService.addByBloomFilter(NAME_BLOOM_FILTER_HELPER, NAME_BLOOM_FILTER_KEY, name);
    }

    /**
     * 查询用户名
     */
    public boolean getName(String name) {
        if (redisService.includeByBloomFilter(NAME_BLOOM_FILTER_HELPER, NAME_BLOOM_FILTER_KEY, name)) {
            log.info("存在用户：{}", name);
            return true;
        }
        return false;
    }
}
