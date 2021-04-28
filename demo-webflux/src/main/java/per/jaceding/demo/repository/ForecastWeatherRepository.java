package per.jaceding.demo.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import per.jaceding.demo.entity.ForecastWeather;
import reactor.core.publisher.Mono;

/**
 * 预测天气持久层
 *
 * @author jaceding
 * @date 2021/4/25
 */
@Repository
public interface ForecastWeatherRepository extends ReactiveCrudRepository<ForecastWeather, Long> {

    /**
     * 根据气象局城市编号获取预测天气
     *
     * @param wbCityCode 气象局城市编号
     * @return 预测天气
     */
    @Query("SELECT * FROM forecast_weather WHERE wb_city_code = :wbCityCode ORDER BY id LIMIT 1;")
    Mono<ForecastWeather> getByWbCityCode(String wbCityCode);
}
