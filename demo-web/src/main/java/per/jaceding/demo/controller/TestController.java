package per.jaceding.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.jaceding.demo.service.TestService;

/**
 * 测试相关
 *
 * @author jaceding
 * @date 2020/6/18
 */
@Api(tags = "测试API")
@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @ApiOperation("添加姓名")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name", value = "名字", required = true, dataTypeClass = String.class)
    })
    @GetMapping("/addName")
    public ResponseEntity<String> addName(String name) {
        testService.addName(name);
        return ResponseEntity.ok("successful");
    }

    @ApiOperation("查询姓名是否存在")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name", value = "名字", required = true, dataTypeClass = String.class)
    })
    @GetMapping("/getName")
    public ResponseEntity<String> getName(String name) {
        return ResponseEntity.ok(String.valueOf(testService.getName(name)));
    }
}
