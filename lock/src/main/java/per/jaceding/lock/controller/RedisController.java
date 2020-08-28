package per.jaceding.lock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.jaceding.common.annotation.ApiResponseDoc;
import per.jaceding.common.vo.Response;
import per.jaceding.lock.service.RedisService;

/**
 * 简易版 分布式锁测试
 *
 * @author jaceding
 * @date 2020/8/21
 */
@RequestMapping("redis")
@ApiResponseDoc
@RestController
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/test1")
    public Response<Object> test1() {
        return Response.success(redisService.test1());
    }
}
