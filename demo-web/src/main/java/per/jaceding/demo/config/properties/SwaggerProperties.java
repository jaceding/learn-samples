package per.jaceding.demo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Swagger 相关属性
 *
 * @author jaceding
 * @date 2020/7/21
 */
@Data
@Component
@ConfigurationProperties(prefix = "knife4j.swagger")
public class SwaggerProperties {
    /**
     * 扫描的基本包
     */
    private String basePackage;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 联系人网址
     */
    private String contactUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 标题
     */
    private String title;

    /**
     * 网址
     */
    private String url;

    /**
     * 版本
     */
    private String version;

    /**
     * 自定义参数配置
     */
    @NestedConfigurationProperty
    private List<ParameterConfig> parameterConfig;

    /**
     * 自定义参数配置
     */
    @Data
    public static class ParameterConfig {

        /**
         * 名称
         */
        private String name;

        /**
         * 描述
         */
        private String description;

        /**
         * 参数类型
         * query, header, path, cookie, form, formData, body
         */
        private String type = "header";

        /**
         * 是否必填
         */
        private boolean required = false;
    }
}
