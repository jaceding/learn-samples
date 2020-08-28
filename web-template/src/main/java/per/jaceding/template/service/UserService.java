package per.jaceding.template.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.service.IService;
import per.jaceding.template.domain.User;
import per.jaceding.template.vo.UserDetailVo;
import per.jaceding.template.vo.UserVo;

import java.util.List;

/**
 * 用户 业务层接口
 *
 * @author jaceding
 * @date 2020/8/28
 */
public interface UserService extends IService<User> {

    /**
     * 获取用户详细信息
     *
     * @param id 用户id
     * @return 用户详细信息
     */
    UserDetailVo getDetail(Integer id);

    /**
     * 获取用户列表
     *
     * @param username 用户名
     * @param orders   排序字段信息
     * @return 用户列表
     */
    List<UserVo> getList(String username, List<OrderItem> orders);

    /**
     * 分页获取用户列表
     *
     * @param username 用户名
     * @param current  当前页
     * @param size     每页显示条数
     * @param orders   排序字段信息
     * @return 用户列表
     */
    IPage<UserVo> getPage(String username, Integer current, Integer size, List<OrderItem> orders);

    /**
     * 添加用户信息
     *
     * @param userVo 用户信息
     * @return 用户信息
     */
    UserVo addUser(UserVo userVo);

    /**
     * 修改用户信息
     *
     * @param userVo 用户信息
     * @return 用户信息
     */
    UserVo modifyUser(UserVo userVo);

    /**
     * 删除用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    UserVo deleteUser(Integer id);
}
