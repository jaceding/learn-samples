package per.jaceding.demo.exception;

import cn.hutool.core.date.DateUtil;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import per.jaceding.demo.vo.ExceptionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 全局异常 controller
 *
 * @author jaceding
 * @date 2021/1/13
 */
@Controller
@SuppressWarnings("deprecation")
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;

    public GlobalErrorController(ErrorAttributes errorAttributes, List<ErrorViewResolver> errorViewResolvers, ServerProperties serverProperties) {
        super(errorAttributes, errorViewResolvers);
        this.errorProperties = serverProperties.getError();
    }

    @RequestMapping
    public ResponseEntity<ExceptionVO> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        Map<String, Object> attributes = getErrorAttributes(request, getErrorAttributeOptions(request));

        HttpStatus httpStatus = HttpStatus.valueOf((Integer) attributes.getOrDefault("status", HttpStatus.BAD_REQUEST.value()));
        ExceptionVO exceptionVO = ExceptionVO.builder()
                .timestamp(DateUtil.toLocalDateTime((Date) attributes.getOrDefault("timestamp", new Date())))
                .path((String) attributes.getOrDefault("path", ""))
                .status((Integer) attributes.getOrDefault("status", HttpStatus.BAD_REQUEST.value()))
                .error((String) attributes.getOrDefault("error", ""))
                .message((String) attributes.getOrDefault("message", ""))
                .build();
        return ResponseEntity.status(httpStatus)
                .body(exceptionVO);
    }


    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ExceptionVO> mediaTypeNotAcceptableHandler(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        return ResponseEntity.status(status).build();
    }

    protected ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (this.errorProperties.isIncludeException()) {
            options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        }
        if (isIncludeStackTrace(request)) {
            options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        if (isIncludeMessage(request)) {
            options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        }
        if (isIncludeBindingErrors(request)) {
            options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        }
        return options;
    }

    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request the source request
     * @return if the stacktrace attribute should be included
     */
    protected boolean isIncludeStackTrace(HttpServletRequest request) {
        switch (getErrorProperties().getIncludeStacktrace()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
            case ON_TRACE_PARAM:
                return getTraceParameter(request);
            default:
                return false;
        }
    }

    /**
     * Determine if the message attribute should be included.
     *
     * @param request the source request
     * @return if the message attribute should be included
     */
    protected boolean isIncludeMessage(HttpServletRequest request) {
        switch (getErrorProperties().getIncludeMessage()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getMessageParameter(request);
            default:
                return false;
        }
    }

    /**
     * Determine if the errors attribute should be included.
     *
     * @param request the source request
     * @return if the errors attribute should be included
     */
    protected boolean isIncludeBindingErrors(HttpServletRequest request) {
        switch (getErrorProperties().getIncludeBindingErrors()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getErrorsParameter(request);
            default:
                return false;
        }
    }

    /**
     * Provide access to the error properties.
     *
     * @return the error properties
     */
    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }
}
