package per.jaceding.template.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 *
 * @author jaceding
 * @date 2020/8/27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_order")
public class Order implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 订单创建者
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 产品id
     */
    @TableField(value = "product_id")
    private Integer productId;

    /**
     * 商品数量
     */
    @TableField(value = "product_num")
    private Integer productNum;

    /**
     * 商品单价
     */
    @TableField(value = "product_price")
    private BigDecimal productPrice;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @TableField(value = "modify_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 删除标志 1:已删除，0:未删除
     */
    @TableLogic
    @TableField(value = "deleted")
    private Boolean deleted;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_ORDER_NO = "order_no";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_PRODUCT_ID = "product_id";

    public static final String COL_PRODUCT_NUM = "product_num";

    public static final String COL_PRODUCT_PRICE = "product_price";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_MODIFY_TIME = "modify_time";

    public static final String COL_DELETED = "deleted";
}