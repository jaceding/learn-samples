package per.jaceding.lock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.jaceding.common.annotation.ApiResponseDoc;
import per.jaceding.common.vo.Response;
import per.jaceding.lock.service.GoodsService;

/**
 * 不加锁测试
 *
 * @author jaceding
 * @date 2020/8/21
 */
@RequestMapping("goods")
@ApiResponseDoc
@RestController
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("/test1")
    public Response<Object> test1() {
        return Response.success(goodsService.test1());
    }
}
