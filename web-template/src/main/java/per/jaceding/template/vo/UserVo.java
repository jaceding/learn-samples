package per.jaceding.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户VO
 *
 * @author jaceding
 * @date 2020/8/26
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    /**
     * 主键
     */
    @Min(value = 1)
    @ApiModelProperty("用户id")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 余额
     */
    @Min(value = 0)
    @ApiModelProperty("余额")
    private BigDecimal balance;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
