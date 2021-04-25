package per.jaceding.demo.enums;

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
     * 没有内容
     */
    NO_CONTENT(204, "没有内容"),

    /**
     * 错误的请求
     **/
    FAIL(400, "错误的请求"),

    /**
     * 暂未登录或token已经过期
     **/
    UNAUTHORIZED(401, "暂未登录或token已经过期"),

    /**
     * 没有权限
     **/
    NOT_PERMISSION(403, "没有权限"),

    /**
     * 你请求的资源不存在
     **/
    NOT_FOUND(404, "你请求的资源不存在"),

    /**
     * 不允许的请求方法
     */
    METHOD_NOT_ALLOWED(405, "不允许的请求方法"),

    /**
     * 服务器异常
     **/
    SERVER_EXCEPTION(500, "服务器异常"),

    ;

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
