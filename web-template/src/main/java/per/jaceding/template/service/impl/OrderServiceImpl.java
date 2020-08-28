package per.jaceding.template.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.jaceding.template.domain.Order;
import per.jaceding.template.mapper.OrderMapper;
import per.jaceding.template.service.OrderService;

/**
 * 订单 业务层实现
 *
 * @author jaceding
 * @date 2020/8/28
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
