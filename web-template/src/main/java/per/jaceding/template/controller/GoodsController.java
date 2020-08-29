package per.jaceding.template.controller;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import per.jaceding.common.annotation.ApiResponseDoc;
import per.jaceding.common.vo.Response;
import per.jaceding.template.domain.Goods;
import per.jaceding.template.service.GoodsService;
import per.jaceding.template.vo.GoodsDetailVo;
import per.jaceding.template.vo.GoodsVo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 商品 控制层
 *
 * @author jaceding
 * @date 2020/8/26
 */
@Validated
@Slf4j
@Api(tags = "商品模块")
@ApiResponseDoc
@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @ApiOperation("获取商品列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "goodsName", value = "商品名"),
            @ApiImplicitParam(name = "orders", value = "排序字段信息，参考值：" + ordersString)
    })
    @GetMapping("/list")
    public Response<List<GoodsDetailVo>> getList(@RequestParam(value = "goodsName", required = false) String goodsName,
                                                 @RequestParam(value = "orders", required = false) List<OrderItem> orders) {
        // 校验排序字段是否合法
        checkOrders(orders);
        return Response.success(goodsService.getList(goodsName, orders));
    }

    @ApiOperation("分页获取商品列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "goodsName", value = "商品名"),
            @ApiImplicitParam(name = "current", value = "当前页，默认值：1"),
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认值：10"),
            @ApiImplicitParam(name = "orders", value = "排序字段信息，参考值：" + ordersString)
    })
    @GetMapping("/page")
    public Response<IPage<GoodsDetailVo>> getPage(@RequestParam(value = "goodsName", required = false) String goodsName,
                                                  @RequestParam(value = "current", defaultValue = "1")
                                                  @Min(value = 1, message = "current最小值为1") Integer current,
                                                  @RequestParam(value = "size", defaultValue = "10")
                                                  @Min(value = 1, message = "size最小值为1")
                                                  @Max(value = 50, message = "size最大值为50") Integer size,
                                                  @RequestParam(value = "orders", required = false) List<OrderItem> orders) {
        // 校验排序字段是否合法
        checkOrders(orders);
        return Response.success(goodsService.getPage(goodsName, current, size, orders));
    }

    @ApiOperation("添加商品")
    @PostMapping
    public Response<GoodsVo> add(@RequestBody GoodsVo goodsVo) {
        return Response.success(goodsService.addGoods(goodsVo));
    }

    @ApiOperation("修改商品")
    @PutMapping
    public Response<GoodsVo> modify(@RequestBody GoodsVo goodsVo) {
        return Response.success(goodsService.modifyGoods(goodsVo));
    }

    @ApiOperation("删除商品信息")
    @DeleteMapping("/{id}")
    public Response<GoodsVo> delete(@PathVariable("id") Integer id) {
        return Response.success(goodsService.deleteGoods(id));
    }

    @ApiOperation("测试缓存失效")
    @PostMapping("/test1")
    public Response<Object> test1() {
        goodsService.test1();
        return Response.success();
    }

    @ApiOperation("测试乐观锁（版本号）")
    @PostMapping("/test2")
    public Response<Object> test2() {
        goodsService.test2();
        return Response.success();
    }

    @ApiOperation("测试事务")
    @PostMapping("/test3")
    public Response<Object> test3() {
        goodsService.test3();
        return Response.success();
    }

    /**
     * 合法的排序字段
     */
    private final String[] legalOrders = new String[]{Goods.COL_ID, Goods.COL_PRICE,
            Goods.COL_CREATE_TIME, Goods.COL_RESIDUE_NUM};

    /**
     * 排序字段
     */
    private final String ordersString = Goods.COL_ID + ", " + Goods.COL_PRICE + ", " +
            Goods.COL_CREATE_TIME + ", " + Goods.COL_RESIDUE_NUM;

    /**
     * 检验排序字段是否合法
     *
     * @param orders 排序字段
     */
    private void checkOrders(List<OrderItem> orders) {
        if (ArrayUtil.isNotEmpty(orders)) {
            for (OrderItem order : orders) {
                if (!ArrayUtil.contains(legalOrders, order.getColumn())) {
                    throw new RuntimeException("非法的排序字段：" + order.getColumn());
                }
            }
        }
    }
}
