package per.jaceding.demo.service;

import org.springframework.stereotype.Service;
import per.jaceding.demo.entity.ForecastWeather;
import per.jaceding.demo.entity.RealtimeWeather;
import per.jaceding.demo.repository.ForecastWeatherRepository;
import per.jaceding.demo.repository.RealtimeWeatherRepository;
import per.jaceding.demo.vo.ForecastWeatherVO;
import per.jaceding.demo.vo.RealtimeWeatherVO;
import per.jaceding.demo.vo.WeatherVO;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * 天气 业务层
 *
 * @author jaceding
 * @date 2021/4/25
 */
@Service
public class WeatherService {

    private final ForecastWeatherRepository forecastWeatherRepository;

    private final RealtimeWeatherRepository realtimeWeatherRepository;

    public WeatherService(ForecastWeatherRepository forecastWeatherRepository, RealtimeWeatherRepository realtimeWeatherRepository) {
        this.forecastWeatherRepository = forecastWeatherRepository;
        this.realtimeWeatherRepository = realtimeWeatherRepository;
    }

    public Mono<WeatherVO> getWeather(String wbCityCode) {
        return realtimeWeatherRepository.getByWbCityCode(wbCityCode)
                .zipWith(forecastWeatherRepository.getByWbCityCode(wbCityCode))
                .map(m -> getWeatherVO(m.getT1(), m.getT2()));
    }

    private WeatherVO getWeatherVO(RealtimeWeather rtWeather, ForecastWeather fcWeather) {
        RealtimeWeatherVO rtWeatherVO = RealtimeWeatherVO.builder()
                .weather(rtWeather.getWeather())
                .temperature(rtWeather.getTemperature())
                .humidity(rtWeather.getHumidity())
                .wd(rtWeather.getWd())
                .wp(rtWeather.getWp())
                .ws(rtWeather.getWs())
                .aqi(rtWeather.getAqi())
                .visibility(rtWeather.getVisibility())
                .rainfall(rtWeather.getRainfall())
                .pressure(rtWeather.getPressure())
                .updateTime(rtWeather.getCreateTime())
                .build();
        ForecastWeatherVO fcWeatherVO = ForecastWeatherVO.builder()
                .blueSky(fcWeather.getBlueSky())
                .dayWeather(fcWeather.getDayWeather())
                .nightWeather(fcWeather.getNightWeather())
                .dayTemperature(fcWeather.getDayTemperature())
                .nightTemperature(fcWeather.getNightTemperature())
                .dayWd(fcWeather.getDayWd())
                .nightWd(fcWeather.getNightWd())
                .dayWp(fcWeather.getDayWp())
                .nightWp(fcWeather.getNightWp())
                .sunriseTime(fcWeather.getSunriseTime())
                .sunsetTime(fcWeather.getSunsetTime())
                .build();
        return WeatherVO.builder()
                .date(LocalDateTime.now())
                .wbCityCode(rtWeather.getWbCityCode())
                .provinceName(rtWeather.getProvinceName())
                .cityName(rtWeather.getCityName())
                .countyName(rtWeather.getCountyName())
                .realtimeWeather(rtWeatherVO)
                .forecastWeather(fcWeatherVO)
                .build();
    }
}
