package per.jaceding.demo.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import per.jaceding.demo.entity.RealtimeWeather;
import reactor.core.publisher.Mono;

/**
 * 实时天气 持久层
 *
 * @author jaceding
 * @date 2021/4/25
 */
public interface RealtimeWeatherRepository extends ReactiveCrudRepository<RealtimeWeather, Long> {

    /**
     * 根据气象局城市编号获取实时天气
     *
     * @param wbCityCode 气象局城市编号
     * @return 预测天气
     */
    @Query("SELECT * FROM realtime_weather WHERE wb_city_code = :wbCityCode ORDER BY id LIMIT 1;")
    Mono<RealtimeWeather> getByWbCityCode(String wbCityCode);
}
