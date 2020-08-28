package per.jaceding.template.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import per.jaceding.template.config.MybatisRedisCache;
import per.jaceding.template.domain.User;
import per.jaceding.template.vo.UserDetailVo;
import per.jaceding.template.vo.UserVo;

import java.util.List;

/**
 * 用户 持久层
 *
 * @author jaceding
 * @date 2020/8/27
 */
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class, properties = {
        @Property(name = "size", value = "1024")
})
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户详细信息
     * 连表查询不使用二级缓存：@Options(useCache = false)
     *
     * @param id 用户id
     * @return 用户详细信息
     */
    @Select("SELECT id, username, password, balance, create_time FROM tb_user u WHERE u.id = #{id}")
    @Results(value = {
            @Result(property = "goods", column = "id",
                    many = @Many(select = "per.jaceding.template.mapper.GoodsMapper.selectListByUserId"))
    })
    @Options(useCache = false)
    UserDetailVo getDetail(Integer id);

    /**
     * 查询用户列表
     *
     * @param userQueryWrapper 查询条件
     * @return 用户信息列表
     */
    @Select("SELECT id, username, balance, create_time FROM tb_user ${ew.customSqlSegment}")
    @ResultType(UserVo.class)
    List<UserVo> getList(@Param(Constants.WRAPPER) QueryWrapper<User> userQueryWrapper);

    /**
     * 分页查询用户列表
     *
     * @param userVoPage       分页信息
     * @param userQueryWrapper 查询条件
     * @return 用户信息列表
     */
    @Select("SELECT id, username, balance, create_time FROM tb_user ${ew.customSqlSegment}")
    @ResultType(UserVo.class)
    IPage<UserVo> getPage(IPage<UserVo> userVoPage, @Param(Constants.WRAPPER) QueryWrapper<User> userQueryWrapper);
}