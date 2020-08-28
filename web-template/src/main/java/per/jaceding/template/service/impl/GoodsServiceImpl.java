package per.jaceding.template.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import per.jaceding.template.domain.Goods;
import per.jaceding.template.mapper.GoodsMapper;
import per.jaceding.template.service.GoodsService;
import per.jaceding.template.vo.GoodsDetailVo;
import per.jaceding.template.vo.GoodsVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 商品 业务层实现
 *
 * @author jaceding
 * @date 2020/8/28
 */
@Slf4j
@Service
@EnableAspectJAutoProxy(exposeProxy = true)
@CacheConfig(cacheNames = "goods")
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Cacheable
    @Override
    public List<GoodsDetailVo> getList(String goodsName, List<OrderItem> orders) {
        return getBaseMapper().getList(getQueryWrapper(goodsName, orders));
    }

    @Cacheable
    @Override
    public IPage<GoodsDetailVo> getPage(String goodsName, Integer current, Integer size, List<OrderItem> orders) {
        IPage<GoodsDetailVo> goodsDetailVoPage = new Page<>(current, size);
        return getBaseMapper().getPage(goodsDetailVoPage, getQueryWrapper(goodsName, orders));
    }

    /**
     * 构建查询条件
     *
     * @param goodsName 商品名
     * @param orders    排序字段信息
     * @return QueryWrapper<User>
     */
    private QueryWrapper<Goods> getQueryWrapper(String goodsName, List<OrderItem> orders) {
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        if (ArrayUtil.isNotEmpty(orders)) {
            for (OrderItem orderItem : orders) {
                goodsQueryWrapper.orderBy(true, orderItem.isAsc(), orderItem.getColumn());
            }
        }
        if (StringUtils.isNotBlank(goodsName)) {
            goodsQueryWrapper.like(Goods.COL_NAME, goodsName);
        }
        return goodsQueryWrapper;
    }

    @CacheEvict(allEntries = true)
    @Override
    public GoodsVo addGoods(GoodsVo goodsVo) {
        Goods goods = new Goods();
        BeanUtil.copyProperties(goodsVo, goods, "id");
        this.save(goods);
        BeanUtil.copyProperties(goods, goodsVo);
        return goodsVo;
    }

    @CacheEvict(allEntries = true)
    @Override
    public GoodsVo modifyGoods(GoodsVo goodsVo) {
        Goods goods = new Goods();
        BeanUtil.copyProperties(goodsVo, goods);
        if (this.updateById(goods)) {
            BeanUtil.copyProperties(goods, goodsVo);
            return goodsVo;
        }
        log.info("修改商品信息");
        throw new RuntimeException("修改商品信息异常");
    }

    @CacheEvict(allEntries = true, condition = "#id > 0")
    @Override
    public GoodsVo deleteGoods(Integer id) {
        Goods goods = this.getById(id);
        if (Objects.nonNull(goods)) {
            GoodsVo goodsVo = new GoodsVo();
            BeanUtil.copyProperties(goods, goodsVo);
            if (this.removeById(goods)) {
                return goodsVo;
            }
        }
        log.info("删除商品信息异常");
        throw new RuntimeException("删除商品信息异常");
    }

    /**
     * 测试缓存失效
     * 注意：在同一个类中调用其他方法，那被调用的方法上面的注解会失效
     * <pre>
     * 直接调用 addGoods(goodsVo); 会导致缓存失效
     *
     * 解决方法1：
     *    GoodsService goodsService = SpringUtil.getBean(GoodsService.class);
     *    goodsService.addGoods(goodsVo);
     * 解决方法2：
     *    首先需要先暴露代理(@EnableAspectJAutoProxy(exposeProxy = true))，否则会抛出异常
     *    然后通过 AopContext.currentProxy() 获取代理对象
     * </pre>
     */
    @Override
    public String test1() {
        GoodsVo goodsVo = new GoodsVo();
        goodsVo.setUserId(5);
        int i = ThreadLocalRandom.current().nextInt(10000);
        goodsVo.setName("Test" + ThreadLocalRandom.current().nextInt(10000));
        goodsVo.setPrice(BigDecimal.valueOf(i));
        goodsVo.setResidueNum(ThreadLocalRandom.current().nextInt(50));

        ((GoodsService) AopContext.currentProxy()).addGoods(goodsVo);
        return "test";
    }
}
