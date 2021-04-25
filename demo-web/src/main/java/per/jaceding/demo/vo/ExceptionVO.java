package per.jaceding.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 异常VO
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("异常VO")
public class ExceptionVO {

    @ApiModelProperty("timestamp")
    private LocalDateTime timestamp;

    @ApiModelProperty("path")
    private String path;

    @ApiModelProperty("status")
    private Integer status;

    @ApiModelProperty("error")
    private String error;

    @ApiModelProperty("message")
    private String message;
}
