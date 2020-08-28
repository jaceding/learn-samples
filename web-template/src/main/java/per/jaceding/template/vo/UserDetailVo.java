package per.jaceding.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户详细信息
 *
 * @author jaceding
 * @date 2020/8/27
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailVo {

    /**
     * 主键
     */
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
    @ApiModelProperty("余额")
    private BigDecimal balance;

    /**
     * 商品列表
     */
    @ApiModelProperty("商品列表")
    private List<GoodsVo> goods;
}
