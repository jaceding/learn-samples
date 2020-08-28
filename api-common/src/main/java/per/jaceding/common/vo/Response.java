package per.jaceding.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import per.jaceding.common.enums.ResponseEnum;

/**
 * HTTP响应 DTO
 *
 * @author jaceding
 * @date 2020/7/22
 */
@SuppressWarnings("unused")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    /**
     * 响应码
     */
    @ApiModelProperty("响应码")
    private int code;
    /**
     * 响应信息
     */
    @ApiModelProperty("描述")
    private String message;
    /**
     * 响应数据
     */
    @ApiModelProperty("响应数据")
    private T data;

    public static Response<Object> doResponse(Integer code, String message) {
        return Response.builder()
                .code(code)
                .message(message)
                .build();
    }

    @SuppressWarnings("all")
    public static <T> Response<T> doResponse(Integer code, String message, T data) {
        return (Response<T>) Response.builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static Response<Object> doResponse(ResponseEnum responseEnum) {
        return doResponse(responseEnum.getCode(), responseEnum.getMessage());
    }

    public static <T> Response<T> doResponse(ResponseEnum responseEnum, T data) {
        return doResponse(responseEnum.getCode(), responseEnum.getMessage(), data);
    }

    public static Response<Object> success() {
        return doResponse(ResponseEnum.SUCCESS);
    }

    public static <T> Response<T> success(T data) {
        return doResponse(ResponseEnum.SUCCESS, data);
    }

    public static <T> Response<T> success(String message, T data) {
        return doResponse(ResponseEnum.SUCCESS.getCode(), message, data);
    }

    public static Response<Object> fail() {
        return doResponse(ResponseEnum.FAIL);
    }

    public static Response<Object> fail(String message) {
        return doResponse(ResponseEnum.FAIL.getCode(), message, null);
    }

    public static <T> Response<T> fail(T data) {
        return doResponse(ResponseEnum.FAIL, data);
    }

    public static <T> Response<T> fail(String message, T data) {
        return doResponse(ResponseEnum.FAIL.getCode(), message, data);
    }
}