package per.jaceding.demo.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import per.jaceding.demo.entity.ForecastWeather;
import per.jaceding.demo.entity.RealtimeWeather;
import per.jaceding.demo.service.ForecastWeatherService;
import per.jaceding.demo.service.RealtimeWeatherService;
import per.jaceding.demo.service.WeatherService;
import per.jaceding.demo.util.NowApiUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * NowApi 任务调度
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Slf4j
@SuppressWarnings("unchecked")
//@Component
public class NowApiSchedule {

    private final RealtimeWeatherService realtimeWeatherService;

    private final ForecastWeatherService forecastWeatherService;

    private final WeatherService weatherService;

    public NowApiSchedule(RealtimeWeatherService realtimeWeatherService, ForecastWeatherService forecastWeatherService, WeatherService weatherService) {
        this.realtimeWeatherService = realtimeWeatherService;
        this.forecastWeatherService = forecastWeatherService;
        this.weatherService = weatherService;
    }

    /**
     * 批量同步天气数据
     */
    @Async
    @Scheduled(cron = "0 20 0/1 * * ?")
    public void bathSyncRealtimeWeather() {
        log.info("开始批量同步天气");
        try {
            // 获取国内天气数据
            Map<String, Object> map = NowApiUtil.getBatchRealtimeWeather(false);
            if (Objects.nonNull(map) && map.containsKey(NowApiUtil.REALTIME_KEY) && map.containsKey(NowApiUtil.FORECAST_KEY)) {
                List<RealtimeWeather> rwList = (List<RealtimeWeather>) map.get(NowApiUtil.REALTIME_KEY);
                realtimeWeatherService.saveBatch(rwList);
                forecastWeatherService.saveBatch((Collection<ForecastWeather>) map.get(NowApiUtil.FORECAST_KEY));

                // 更新气象局城市编号
                weatherService.addWbCityCodes(rwList.stream()
                        .map(RealtimeWeather::getWbCityCode)
                        .collect(Collectors.toSet()));
            }
        } catch (Throwable e) {
            log.info("批量同步天气数据失败", e);
        } finally {
            log.info("批量同步天气完毕");
            weatherService.clearCache();
        }
    }
}
