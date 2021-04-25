package per.jaceding.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 实时天气VO
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("实时天气VO")
public class RealtimeWeatherVO {

    @ApiModelProperty("天气")
    private String weather;

    @ApiModelProperty("温度，单位：℃")
    private Double temperature;

    @ApiModelProperty("相对湿度，单位：%")
    private Double humidity;

    @ApiModelProperty("风向")
    private String wd;

    @ApiModelProperty("风力，单位：级")
    private Double wp;

    @ApiModelProperty("风速，单位：km/h")
    private Double ws;

    @ApiModelProperty("pm2.5")
    private Double aqi;

    @ApiModelProperty("能见度，单位：km")
    private Double visibility;

    @ApiModelProperty("降雨量，单位：mm")
    private Double rainfall;

    @ApiModelProperty("气压，单位：hpa")
    private Double pressure;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
