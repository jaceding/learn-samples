package per.jaceding.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品VO
 *
 * @author jaceding
 * @date 2020/8/26
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo {

    /**
     * 主键
     */
    @ApiModelProperty("商品id")
    private Integer id;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 库存数量
     */
    @ApiModelProperty("库存数量")
    private Integer residueNum;

    /**
     * 商品单价
     */
    @ApiModelProperty("商品单价")
    private BigDecimal price;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 商品发布者id
     */
    @ApiModelProperty("商品发布者id")
    private Integer userId;
}
