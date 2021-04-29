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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户信息
 *
 * @author jaceding
 * @date 2021/4/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "users")
public class UserDocument {

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 用户名
     */
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
    private String fullName;

    /**
     * 手机号
     */
    @Field(type = FieldType.Keyword)
    private String cellPhone;

    /**
     * email
     */
    @Field(type = FieldType.Keyword)
    private String email;

    /**
     * qq
     */
    @Field(type = FieldType.Keyword)
    private String qq;

    /**
     * wechat
     */
    @Field(type = FieldType.Keyword)
    private String wechat;

    /**
     * 性别
     */
    @Field(type = FieldType.Integer)
    private Integer sex;

    /**
     * 出生日期
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate birthday;

    /**
     * 身高
     */
    @Field(type = FieldType.Double)
    private Double height;

    /**
     * 体重
     */
    @Field(type = FieldType.Double)
    private Double weight;

    /**
     * 国籍
     */
    @Field(type = FieldType.Keyword)
    private String nationality;

    /**
     * 名族
     */
    @Field(type = FieldType.Keyword)
    private String nation;

    /**
     * 户籍
     */
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
    private String domicile;

    /**
     * 住址
     */
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
    private String address;

    /**
     * 语言
     */
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
    private String language;

    /**
     * 婚姻状态
     */
    @Field(type = FieldType.Integer)
    private Integer marriage;

    /**
     * 宗教信仰
     */
    @Field(type = FieldType.Keyword)
    private String faith;

    /**
     * 政治面貌
     */
    @Field(type = FieldType.Keyword)
    private String political;

    /**
     * 座右铭
     */
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
    private String motto;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime updateTime;
}
