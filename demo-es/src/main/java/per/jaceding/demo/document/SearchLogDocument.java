package per.jaceding.demo.document;

import cn.hutool.core.date.DatePattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * 查询日志信息
 *
 * @author jaceding
 * @date 2021/4/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "search-logs")
public class SearchLogDocument {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 类型
     */
    @Field(type = FieldType.Keyword)
    private String type;

    /**
     * 页码
     */
    @Field(type = FieldType.Long)
    private Long page;

    /**
     * 索引
     */
    @Field(type = FieldType.Long)
    private Long index;

    /**
     * 耗时，单位毫秒
     */
    @Field(type = FieldType.Long)
    private Long duration;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;
}
