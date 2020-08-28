package per.jaceding.template.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import per.jaceding.template.domain.Goods;
import per.jaceding.template.vo.GoodsDetailVo;
import per.jaceding.template.vo.GoodsVo;

import java.util.List;

/**
 * 商品 持久层
 *
 * @author jaceding
 * @date 2020/8/27
 */
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 根据用户id查询商品列表
     *
     * @param userId 用户id
     * @return 商品列表
     */
    @Select("SELECT id, name, residue_num, price, user_id, create_time FROM tb_goods WHERE user_id = #{userId}")
    @ResultType(GoodsVo.class)
    List<GoodsVo> selectListByUserId(@Param("userId") String userId);

    /**
     * 查询商品列表
     *
     * @param queryWrapper 查询条件
     * @return 商品列表
     */
    @Select(value = "SELECT g.id,g.`name`,g.residue_num,g.price,g.create_time create_time,u.id uid,u.username username " +
            "FROM tb_goods g LEFT JOIN tb_user u ON g.user_id = u.id ${ew.customSqlSegment}")
    @Results(id = "goodsDetailVoResultMap", value = {
            // 只需映射不同的字段
            @Result(property = "user.id", column = "uid"),
            @Result(property = "user.username", column = "username")
    })
    List<GoodsDetailVo> getList(@Param(Constants.WRAPPER) QueryWrapper<Goods> queryWrapper);

    /**
     * 分页查询商品列表
     *
     * @param goodsDetailVoPage 分页信息
     * @param queryWrapper      查询条件
     * @return 商品列表
     */
    @Select(value = "SELECT g.id,g.`name`,g.residue_num,g.price,g.create_time create_time,u.id uid,u.username username " +
            "FROM tb_goods g LEFT JOIN tb_user u ON g.user_id = u.id ${ew.customSqlSegment}")
    @ResultMap("goodsDetailVoResultMap")
    IPage<GoodsDetailVo> getPage(IPage<GoodsDetailVo> goodsDetailVoPage, @Param(Constants.WRAPPER) QueryWrapper<Goods> queryWrapper);
}