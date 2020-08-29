package per.jaceding.template.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.service.IService;
import per.jaceding.template.domain.Goods;
import per.jaceding.template.vo.GoodsDetailVo;
import per.jaceding.template.vo.GoodsVo;

import java.util.List;

/**
 * 商品 业务层接口
 *
 * @author jaceding
 * @date 2020/8/28
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     *
     * @param goodsName 商品名
     * @param orders    排序字段信息
     * @return 用户列表
     */
    List<GoodsDetailVo> getList(String goodsName, List<OrderItem> orders);

    /**
     * 分页获取商品列表
     *
     * @param goodsName 商品名
     * @param current   当前页
     * @param size      每页显示条数
     * @param orders    排序字段信息
     * @return 商品列表
     */
    IPage<GoodsDetailVo> getPage(String goodsName, Integer current, Integer size, List<OrderItem> orders);

    /**
     * 增加商品
     *
     * @param goodsVo 商品信息
     * @return 商品信息
     */
    GoodsVo addGoods(GoodsVo goodsVo);

    /**
     * 修改商品信息
     *
     * @param goodsVo 商品信息
     * @return 商品信息
     */
    GoodsVo modifyGoods(GoodsVo goodsVo);

    /**
     * 删除商品信息
     *
     * @param id 商品信息id
     * @return 商品信息
     */
    GoodsVo deleteGoods(Integer id);

    /**
     * 测试缓存失效
     */
    void test1();

    /**
     * 测试乐观锁（版本号）
     */
    void test2();

    /**
     * 测试事务
     */
    void test3();
}
