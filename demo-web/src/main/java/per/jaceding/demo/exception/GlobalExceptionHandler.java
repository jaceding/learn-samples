package per.jaceding.demo.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import per.jaceding.demo.vo.ExceptionVO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

/**
 * 全局异常处理器
 *
 * @author jaceding
 * @date 2021/1/9
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionVO> exceptionHandler(HttpServletRequest request) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ExceptionVO.builder()
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .status(INTERNAL_SERVER_ERROR.value())
                        .error(INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message("")
                        .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionVO> httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest request,
                                                                                     Exception e) {
        return ResponseEntity.status(METHOD_NOT_ALLOWED)
                .body(ExceptionVO.builder()
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .status(METHOD_NOT_ALLOWED.value())
                        .error(METHOD_NOT_ALLOWED.getReasonPhrase())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionVO> missingServletRequestParameterExceptionHandler(HttpServletRequest request,
                                                                                      Exception e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionVO.builder()
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.getReasonPhrase())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionVO> businessExceptionHandler(BusinessException e, HttpServletRequest request) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionVO.builder()
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.getReasonPhrase())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ExceptionVO> exceptionHandler(TypeMismatchException e, HttpServletRequest request) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionVO.builder()
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.getReasonPhrase())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<ExceptionVO> httpMediaTypeExceptionHandler(HttpMediaTypeException e, HttpServletRequest request) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionVO.builder()
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.getReasonPhrase())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionVO> illegalArgumentExceptionHandler(IllegalArgumentException e, HttpServletRequest request) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionVO.builder()
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.getReasonPhrase())
                        .message(e.getMessage())
                        .build());
    }
}