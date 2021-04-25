package per.jaceding.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import per.jaceding.demo.util.NowApiUtil;

import java.time.LocalTime;

/**
 * 预测天气VO
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("预测天气VO")
public class ForecastWeatherVO {

    @ApiModelProperty("是否可见蓝天，1:可见蓝天 0:无蓝天")
    private Integer blueSky;

    @ApiModelProperty("白天天气")
    private String dayWeather;

    @ApiModelProperty("夜间天气")
    private String nightWeather;

    @ApiModelProperty("白天温度，单位：℃")
    private Double dayTemperature;

    @ApiModelProperty("夜间温度，单位：℃")
    private Double nightTemperature;

    @ApiModelProperty("白天风向")
    private String dayWd;

    @ApiModelProperty("夜间风向")
    private String nightWd;

    @ApiModelProperty("白天风力")
    private String dayWp;

    @ApiModelProperty("夜间风力")
    private String nightWp;

    @JsonFormat(pattern = NowApiUtil.PATTERN)
    @ApiModelProperty("日出时间")
    private LocalTime sunriseTime;

    @JsonFormat(pattern = NowApiUtil.PATTERN)
    @ApiModelProperty("日落时间")
    private LocalTime sunsetTime;
}
