package per.jaceding.template.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import per.jaceding.template.domain.User;
import per.jaceding.template.mapper.UserMapper;
import per.jaceding.template.service.UserService;
import per.jaceding.template.vo.UserDetailVo;
import per.jaceding.template.vo.UserVo;

import java.util.List;
import java.util.Objects;

/**
 * 用户 业务层实现
 *
 * @author jaceding
 * @date 2020/8/28
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public UserDetailVo getDetail(Integer id) {
        return getBaseMapper().getDetail(id);
    }

    @Override
    public List<UserVo> getList(String username, List<OrderItem> orders) {
        return getBaseMapper().getList(getQueryWrapper(username, orders));
    }

    @Override
    public IPage<UserVo> getPage(String username, Integer current, Integer size, List<OrderItem> orders) {
        IPage<UserVo> userVoPage = new Page<>(current, size);
        return getBaseMapper().getPage(userVoPage, getQueryWrapper(username, orders));
    }

    /**
     * 构建查询条件
     *
     * @param username 用户名
     * @param orders   排序字段信息
     * @return QueryWrapper<User>
     */
    private QueryWrapper<User> getQueryWrapper(String username, List<OrderItem> orders) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (ArrayUtil.isNotEmpty(orders)) {
            for (OrderItem orderItem : orders) {
                userQueryWrapper.orderBy(true, orderItem.isAsc(), orderItem.getColumn());
            }
        }
        if (StringUtils.isNotBlank(username)) {
            userQueryWrapper.like(User.COL_USERNAME, username);
        }
        userQueryWrapper.select(User.COL_ID, User.COL_USERNAME, User.COL_BALANCE);
        return userQueryWrapper;
    }

    @Override
    public UserVo addUser(UserVo userVo) {
        User user = new User();
        BeanUtil.copyProperties(userVo, user, "id");
        user.setPassword("");
        log.info("userVo=" + userVo.toString());
        log.info("user=" + user.toString());
        this.save(user);
        userVo.setId(user.getId());
        return userVo;
    }

    @Override
    public UserVo modifyUser(UserVo userVo) {
        User user = new User();
        BeanUtil.copyProperties(userVo, user);
        log.info("userVo=" + userVo.toString());
        log.info("user=" + user.toString());
        if (this.updateById(user)) {
            BeanUtil.copyProperties(user, userVo);
            return userVo;
        }
        log.info("修改用户信息异常");
        throw new RuntimeException("修改用户信息异常");
    }

    @Override
    public UserVo deleteUser(Integer id) {
        User user = this.getById(id);
        if (Objects.nonNull(user)) {
            UserVo userVo = new UserVo();
            BeanUtil.copyProperties(user, userVo);
            if (this.removeById(id)) {
                return userVo;
            }
        }
        log.info("删除用户异常");
        throw new RuntimeException("删除用户异常");
    }
}
