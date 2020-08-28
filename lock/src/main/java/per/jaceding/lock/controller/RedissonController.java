package per.jaceding.lock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.jaceding.common.annotation.ApiResponseDoc;
import per.jaceding.common.vo.Response;
import per.jaceding.lock.service.RedissonService;

/**
 * 测试Redisson
 *
 * @author jaceding
 * @date 2020/8/20
 */
@RequestMapping("redisson")
@ApiResponseDoc
@RestController
public class RedissonController {

    private final RedissonService redissonService;

    public RedissonController(RedissonService redissonService) {
        this.redissonService = redissonService;
    }

    @GetMapping("/test1")
    public Response<Object> test1() throws InterruptedException {
        return Response.success(redissonService.test1());
    }

    @GetMapping("/test2")
    public Response<Object> test2() {
        return Response.success(redissonService.test2("test2"));
    }
}
