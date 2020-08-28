package per.jaceding.template;

import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import per.jaceding.template.domain.Goods;
import per.jaceding.template.service.GoodsService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author jaceding
 * @date 2020/8/27
 */
@SpringBootTest(classes = Application.class)
class ApplicationTest {

    @Autowired
    GoodsService goodsService;

    @Test
    void test() {
        Goods goods1 = goodsService.getById(5);
        Goods goods2 = goodsService.getById(5);
        goods1.setPrice(goods1.getPrice().add(BigDecimal.valueOf(2)));
        goodsService.updateById(goods1);
        goods2.setName("test version");
        goodsService.updateById(goods2);
    }

    @Autowired
    RedissonClient redissonClient;

    @Test
    void testRedissonClient() {
        RMap<String, LocalDateTime> testMap = redissonClient.getMap("testMap");
        testMap.put("key", LocalDateTime.now());
        LocalDateTime now = testMap.get("key");
        System.out.println(now.toString());
    }

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void testRedisTemplate() {
        redisTemplate.opsForValue().set("key", "hello");
        Object key = redisTemplate.opsForValue().get("key");
        System.out.println(key);
    }
}