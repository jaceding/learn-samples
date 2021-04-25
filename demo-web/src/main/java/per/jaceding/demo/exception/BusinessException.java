package per.jaceding.demo.exception;

/**
 * 业务处理异常
 *
 * @author jaceding
 * @date 2021/1/11
 */
@SuppressWarnings("unused")
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 3868395354276914933L;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}