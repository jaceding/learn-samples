package per.jaceding.lock.service;

import org.springframework.stereotype.Service;

/**
 * 不加锁测试
 *
 * @author jaceding
 * @date 2020/8/21
 */
@Service
public class GoodsService {

    /**
     * 剩余商品库存
     */
    private Integer NUM = 10000;

    /**
     * 模拟减库存
     * 测试结果：1000次并发请求减库存之后，剩余库存大于9000
     */
    public String test1() {
        NUM--;
        return "NUM=" + NUM;
    }
}
