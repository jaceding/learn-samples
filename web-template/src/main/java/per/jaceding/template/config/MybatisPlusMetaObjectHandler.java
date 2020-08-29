package per.jaceding.template.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mybatis plus 自动填充创建时间和修改时间
 * <p>
 * 使用this.strictUpdateFill()填充字段，如果该字段已经有值，会更新不了（也就是如果先查询出来，再更新会更新失败）
 *
 * @author jaceding
 * @date 2020/8/27
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        this.setFieldValByName("createTime", localDateTime, metaObject);
        this.setFieldValByName("modifyTime", localDateTime, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("modifyTime", LocalDateTime.now(), metaObject);
    }
}
