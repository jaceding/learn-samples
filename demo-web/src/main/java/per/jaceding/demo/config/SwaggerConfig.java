package per.jaceding.demo.config;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import per.jaceding.demo.config.properties.SwaggerProperties;
import per.jaceding.demo.enums.ResponseEnum;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Swagger配置
 *
 * @author jaceding
 * @date 2021/1/23
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;

    private final Knife4jProperties knife4jProperties;

    /**
     * Swagger忽略的类
     */
    private final Class<?>[] ignoredParameterTypes = new Class[]{
            ServletRequest.class,
            ServletResponse.class,
            HttpServletRequest.class,
            HttpServletResponse.class,
            HttpSession.class
    };

    public SwaggerConfig(SwaggerProperties swaggerProperties, Knife4jProperties knife4jProperties) {
        this.swaggerProperties = swaggerProperties;
        this.knife4jProperties = knife4jProperties;
    }

    @Bean
    public Docket createRestAp() {
        ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select();
        String basePackage = swaggerProperties.getBasePackage();
        // 如果包路径为空，则默认扫描类上有@Api注解的类
        if (StrUtil.isBlank(basePackage)) {
            apiSelectorBuilder.apis(RequestHandlerSelectors.withClassAnnotation(Api.class));
        } else {
            // 扫描指定的包
            apiSelectorBuilder.apis(RequestHandlerSelectors.basePackage(basePackage));
        }

        return apiSelectorBuilder.paths(PathSelectors.any())
                .build()
                .enable(!knife4jProperties.isProduction())
                .ignoredParameterTypes(ignoredParameterTypes)
                .directModelSubstitute(LocalDateTime.class, String.class)
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalTime.class, String.class)
                .globalRequestParameters(globalRequestParameters())
                .globalResponses(HttpMethod.GET, responseList())
                .globalResponses(HttpMethod.POST, responseList())
                .globalResponses(HttpMethod.PUT, responseList())
                .globalResponses(HttpMethod.DELETE, responseList());
    }

    /**
     * 构建ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .termsOfServiceUrl(swaggerProperties.getUrl())
                .contact(new Contact(
                        swaggerProperties.getContactName(),
                        swaggerProperties.getContactUrl(),
                        swaggerProperties.getContactEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

    /**
     * 构建全局响应状态
     */
    private List<Response> responseList() {
        List<Response> responseList = new ArrayList<>();
        Arrays.stream(ResponseEnum.values())
                .forEach(e -> responseList.add(new ResponseBuilder()
                        .code(String.valueOf(e.getCode()))
                        .description(e.getMessage())
                        .build())
                );
        return responseList;
    }

    /**
     * 构建全局请求参数
     */
    private List<RequestParameter> globalRequestParameters() {
        List<RequestParameter> requestParameterList = new ArrayList<>();
        for (SwaggerProperties.ParameterConfig parameterConfig : swaggerProperties.getParameterConfig()) {
            RequestParameterBuilder builder = new RequestParameterBuilder();
            RequestParameter requestParameter = builder.name(parameterConfig.getName())
                    .description(parameterConfig.getDescription())
                    .in(parameterConfig.getType())
                    .required(parameterConfig.isRequired())
                    .build();
            requestParameterList.add(requestParameter);
        }
        return requestParameterList;
    }
}