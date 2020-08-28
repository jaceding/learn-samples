package per.jaceding.common.enums;

import lombok.Getter;

/**
 * 响应 枚举类
 *
 * @author jaceding
 * @date 2020/7/22
 */
@SuppressWarnings("unused")
@Getter
public enum ResponseEnum {

    /**
     * 操作成功
     **/
    SUCCESS(200, "操作成功"),

    /**
     * 操作失败
     **/
    FAIL(400, "操作失败"),

    /**
     * 非法访问
     **/
    UNAUTHORIZED(401, "非法访问"),

    /**
     * 没有权限
     **/
    NOT_PERMISSION(403, "没有权限"),

    /**
     * 你请求的资源不存在
     **/
    NOT_FOUND(404, "你请求的资源不存在"),

    /**
     * Token无效
     */
    TOKEN_INVALID_EXCEPTION(4001, "Token无效"),

    /**
     * 请求参数异常
     */
    PARAM_EXCEPTION(4003, "请求参数异常"),

    /**
     * 服务器异常
     **/
    SERVER_EXCEPTION(500, "服务器异常"),

    /**
     * 业务处理异常
     **/
    BUSINESS_EXCEPTION(5001, "业务处理异常");

    private final int code;
    private final String message;

    ResponseEnum(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseEnum getResponseEnum(int code) {
        for (ResponseEnum ec : ResponseEnum.values()) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }
}