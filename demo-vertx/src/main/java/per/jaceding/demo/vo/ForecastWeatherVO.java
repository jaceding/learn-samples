package per.jaceding.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ForecastWeatherVO {

    private Integer blueSky;

    private String dayWeather;

    private String nightWeather;

    private Double dayTemperature;

    private Double nightTemperature;

    private String dayWd;

    private String nightWd;

    private String dayWp;

    private String nightWp;

    private LocalTime sunriseTime;

    private LocalTime sunsetTime;
}
