package per.jaceding.demo.vo;

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
public class WeatherVO {

    private LocalDateTime date;

    private String wbCityCode;

    private String provinceName;

    private String cityName;

    private String countyName;

    private RealtimeWeatherVO realtimeWeather;

    private ForecastWeatherVO forecastWeather;
}