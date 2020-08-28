package per.jaceding.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import per.jaceding.template.domain.Order;

/**
 * 订单 持久层
 *
 * @author jaceding
 * @date 2020/8/27
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {
}