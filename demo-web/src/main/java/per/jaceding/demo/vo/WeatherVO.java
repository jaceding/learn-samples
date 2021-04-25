package per.jaceding.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 天气VO
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("天气VO")
public class WeatherVO {

    @ApiModelProperty("日期")
    private LocalDateTime date;

    @ApiModelProperty("气象局城市编号")
    private String wbCityCode;

    @ApiModelProperty("省/自治区/直辖市/特别行政区")
    private String provinceName;

    @ApiModelProperty("市/是否城区")
    private String cityName;

    @ApiModelProperty("区/县/镇/是否城区")
    private String countyName;

    @ApiModelProperty("实时天气")
    private RealtimeWeatherVO realtimeWeather;

    @ApiModelProperty("预测天气")
    private ForecastWeatherVO forecastWeather;
}