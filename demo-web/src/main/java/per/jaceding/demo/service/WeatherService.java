package per.jaceding.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import per.jaceding.demo.constants.RedisConstants;
import per.jaceding.demo.entity.ForecastWeather;
import per.jaceding.demo.entity.RealtimeWeather;
import per.jaceding.demo.vo.ForecastWeatherVO;
import per.jaceding.demo.vo.RealtimeWeatherVO;
import per.jaceding.demo.vo.WeatherVO;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 天气业务层
 *
 * @author jaceding
 * @date 2021/4/9
 */
@CacheConfig(cacheNames = RedisConstants.WEATHER_CACHE)
@Slf4j
@Service
public class WeatherService {
    private final RealtimeWeatherService realtimeWeatherService;
    private final ForecastWeatherService forecastWeatherService;

    public WeatherService(RealtimeWeatherService realtimeWeatherService, ForecastWeatherService forecastWeatherService) {
        this.realtimeWeatherService = realtimeWeatherService;
        this.forecastWeatherService = forecastWeatherService;
    }

    /**
     * 按气象局城市编号查询天气
     *
     * @param wbCityCode 气象局城市编号
     * @return 天气VO
     */
    @Cacheable
    @Transactional(readOnly = true)
    public WeatherVO getByWbCityCode(String wbCityCode) {
        RealtimeWeather rtWeather = realtimeWeatherService.getByWbCityCode(wbCityCode);
        ForecastWeather fcWeather = forecastWeatherService.getByWbCityCode(wbCityCode);
        return getWeatherVO(rtWeather, fcWeather);
    }

    @CacheEvict(allEntries = true)
    public void clearCache() {
        log.info("清除天气缓存：" + RedisConstants.WEATHER_CACHE);
    }

    /**
     * 判断气象局城市编号是否有效
     *
     * @param wbCityCode 气象局城市编号
     * @return 是否有效
     */
    public boolean checkWbCityCode(String wbCityCode) {
        return true;
    }

    /**
     * 更新缓存 WB_CITY_CODE_CACHE
     *
     * @param wbCityCodes 新增的wbCityCodes
     */
    public void addWbCityCodes(Collection<String> wbCityCodes) {

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
