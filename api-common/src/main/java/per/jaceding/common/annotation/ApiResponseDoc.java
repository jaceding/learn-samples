package per.jaceding.common.annotation;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Swagger 响应码集合注解
 *
 * @author jaceding
 * @date 2020/8/20
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 400, message = "操作失败"),
        @ApiResponse(code = 401, message = "非法访问"),
        @ApiResponse(code = 403, message = "没有权限"),
        @ApiResponse(code = 404, message = "资源不存在"),
        @ApiResponse(code = 4001, message = "token无效"),
        @ApiResponse(code = 500, message = "服务器异常"),
        @ApiResponse(code = 4003, message = "参数异常"),
        @ApiResponse(code = 5001, message = "业务异常"),
})
public @interface ApiResponseDoc {
}
