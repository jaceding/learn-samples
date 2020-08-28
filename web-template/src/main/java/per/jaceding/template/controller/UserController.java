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
import per.jaceding.template.domain.User;
import per.jaceding.template.service.UserService;
import per.jaceding.template.vo.UserDetailVo;
import per.jaceding.template.vo.UserVo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 用户 控制层
 *
 * @author jaceding
 * @date 2020/8/26
 */
@Validated
@Slf4j
@Api(tags = "用户模块")
@ApiResponseDoc
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("获取用户详细信息")
    @GetMapping("/{id}")
    public Response<UserDetailVo> getDetail(@PathVariable("id") Integer id) {
        return Response.success(userService.getDetail(id));
    }

    @ApiOperation("获取用户列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "orders", value = "排序字段信息，参考值：" + ordersString)
    })
    @GetMapping("/list")
    public Response<List<UserVo>> getList(@RequestParam(value = "username", required = false) String username,
                                          @RequestParam(value = "orders", required = false) List<OrderItem> orders) {
        // 校验排序字段是否合法
        checkOrders(orders);
        return Response.success(userService.getList(username, orders));
    }

    @ApiOperation("分页获取用户列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "current", value = "当前页，默认值：1"),
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认值：10"),
            @ApiImplicitParam(name = "orders", value = "排序字段信息，参考值：" + ordersString)
    })
    @GetMapping("/page")
    public Response<IPage<UserVo>> getPage(@RequestParam(value = "username", required = false) String username,
                                           @RequestParam(value = "current", defaultValue = "1")
                                           @Min(value = 1, message = "current最小值为1") Integer current,
                                           @RequestParam(value = "size", defaultValue = "10")
                                           @Min(value = 1, message = "size最小值为1")
                                           @Max(value = 50, message = "size最大值为50") Integer size,
                                           @RequestParam(value = "orders", required = false) List<OrderItem> orders) {
        // 校验排序字段是否合法
        checkOrders(orders);
        return Response.success(userService.getPage(username, current, size, orders));
    }

    @ApiOperation("添加用户")
    @PostMapping
    public Response<UserVo> add(@RequestBody @Validated UserVo userVo) {
        return Response.success(userService.addUser(userVo));
    }

    @ApiOperation("修改用户")
    @PutMapping
    public Response<UserVo> modify(@RequestBody @Validated UserVo userVo) {
        return Response.success(userService.modifyUser(userVo));
    }

    @ApiOperation("删除用户")
    @PutMapping("/{id}")
    public Response<UserVo> delete(@PathVariable("id") Integer id) {
        return Response.success(userService.deleteUser(id));
    }

    /**
     * 合法的排序字段
     */
    private final String[] legalOrders = new String[]{User.COL_CREATE_TIME, User.COL_BALANCE, User.COL_ID};

    /**
     * 排序字段
     */
    private final String ordersString = User.COL_CREATE_TIME + ", " + User.COL_BALANCE + ", " +
            User.COL_ID;

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
