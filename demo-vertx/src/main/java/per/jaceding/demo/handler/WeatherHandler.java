package per.jaceding.demo.handler;

import cn.hutool.json.JSONUtil;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowIterator;
import io.vertx.sqlclient.Tuple;
import lombok.extern.slf4j.Slf4j;
import per.jaceding.demo.MySQLPoll;
import per.jaceding.demo.entity.ForecastWeather;
import per.jaceding.demo.entity.RealtimeWeather;
import per.jaceding.demo.vo.ForecastWeatherVO;
import per.jaceding.demo.vo.RealtimeWeatherVO;
import per.jaceding.demo.vo.WeatherVO;

import java.time.LocalDateTime;

/**
 * 天气 handler
 *
 * @author jaceding
 * @date 2021/4/25
 */
@Slf4j
public class WeatherHandler {

    /**
     * 获取天气
     */
    public static void getWeather(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        String wbCityCode = request.getParam("wbCityCode");
        log.info("wbCityCode={}", wbCityCode);

        MySQLPool pool = MySQLPoll.getPool();
        Tuple tuple = Tuple.of(wbCityCode);

        pool.getConnection()
                // Transaction must use a connection
                .onSuccess(conn -> {
                    // Begin the transaction
                    conn.begin()
                            .compose(tx -> conn
                                    // Various statements
                                    .preparedQuery("SELECT * FROM realtime_weather WHERE wb_city_code = ? ORDER BY id LIMIT 1;")
                                    .execute(tuple)
                                    .onSuccess(rs1 -> {
                                        RowIterator<Row> iterator1 = rs1.iterator();
                                        if (rs1.size() == 1) {
                                            RealtimeWeather realtimeWeather = getRealtimeWeather(iterator1.next());
                                            conn.preparedQuery("SELECT * FROM forecast_weather WHERE wb_city_code = ? ORDER BY id LIMIT 1;")
                                                    .execute(tuple)
                                                    .onSuccess(rs2 -> {
                                                        tx.commit();
                                                        RowIterator<Row> iterator2 = rs2.iterator();
                                                        if (rs2.size() == 1) {
                                                            ForecastWeather forecastWeather = getForecastWeather(iterator2.next());
                                                            ctx.response().setStatusCode(200).end(JSONUtil.toJsonStr(getWeatherVO(realtimeWeather, forecastWeather)));
                                                        }
                                                    });
                                        } else {
                                            // Commit the transaction
                                            tx.commit();
                                        }
                                    }))
                            // Return the connection to the pool
                            .eventually(v -> conn.close())
                            .onSuccess(v -> log.info("Transaction succeeded"))
                            .onFailure(err -> log.info("Transaction failed: " + err.getMessage()));
                });
    }

    public static void hello(RoutingContext ctx) {
        ctx.response().setStatusCode(200).end("hello world");
    }

    private static RealtimeWeather getRealtimeWeather(Row row) {
        return RealtimeWeather.builder()
                .id(row.getLong(RealtimeWeather.COL_ID))
                .date(row.getLocalDate(RealtimeWeather.COL_DATE))
                .wbCityCode(row.getString(RealtimeWeather.COL_WB_CITY_CODE))
                .provinceName(row.getString(RealtimeWeather.COL_PROVINCE_NAME))
                .cityName(row.getString(RealtimeWeather.COL_CITY_NAME))
                .countyName(row.getString(RealtimeWeather.COL_COUNTY_NAME))
                .weatherCode(row.getString(RealtimeWeather.COL_WEATHER_CODE))
                .weather(row.getString(RealtimeWeather.COL_WEATHER))
                .temperature(row.getDouble(RealtimeWeather.COL_TEMPERATURE))
                .humidity(row.getDouble(RealtimeWeather.COL_HUMIDITY))
                .wdCode(row.getString(RealtimeWeather.COL_WD_CODE))
                .wd(row.getString(RealtimeWeather.COL_WD))
                .wp(row.getDouble(RealtimeWeather.COL_WP))
                .ws(row.getDouble(RealtimeWeather.COL_WS))
                .aqi(row.getDouble(RealtimeWeather.COL_AQI))
                .visibility(row.getDouble(RealtimeWeather.COL_VISIBILITY))
                .rainfall(row.getDouble(RealtimeWeather.COL_RAINFALL))
                .pressure(row.getDouble(RealtimeWeather.COL_PRESSURE))
                .createTime(row.getLocalDateTime(RealtimeWeather.COL_CREATE_TIME))
                .build();
    }

    private static ForecastWeather getForecastWeather(Row row) {
        return ForecastWeather.builder()
                .id(row.getLong(ForecastWeather.COL_ID))
                .date(row.getLocalDate(ForecastWeather.COL_DATE))
                .wbCityCode(row.getString(ForecastWeather.COL_WB_CITY_CODE))
                .provinceName(row.getString(ForecastWeather.COL_PROVINCE_NAME))
                .cityName(row.getString(ForecastWeather.COL_CITY_NAME))
                .countyName(row.getString(ForecastWeather.COL_COUNTY_NAME))
                .blueSky(row.getInteger(ForecastWeather.COL_BLUE_SKY))
                .dayWeatherCode(row.getString(ForecastWeather.COL_DAY_WEATHER_CODE))
                .dayWeather(row.getString(ForecastWeather.COL_DAY_WEATHER))
                .nightWeatherCode(row.getString(ForecastWeather.COL_NIGHT_WEATHER_CODE))
                .nightWeather(row.getString(ForecastWeather.COL_NIGHT_WEATHER))
                .dayTemperature(row.getDouble(ForecastWeather.COL_DAY_TEMPERATURE))
                .nightTemperature(row.getDouble(ForecastWeather.COL_NIGHT_TEMPERATURE))
                .dayWdCode(row.getString(ForecastWeather.COL_DAY_WD_CODE))
                .dayWd(row.getString(ForecastWeather.COL_DAY_WD))
                .nightWdCode(row.getString(ForecastWeather.COL_NIGHT_WD_CODE))
                .nightWd(row.getString(ForecastWeather.COL_NIGHT_WD))
                .dayWpCode(row.getString(ForecastWeather.COL_DAY_WD_CODE))
                .dayWp(row.getString(ForecastWeather.COL_DAY_WP))
                .nightWpCode(row.getString(ForecastWeather.COL_NIGHT_WP_CODE))
                .nightWp(row.getString(ForecastWeather.COL_NIGHT_WP))
                .sunriseTime(row.getLocalTime(ForecastWeather.COL_SUNRISE_TIME))
                .sunsetTime(row.getLocalTime(ForecastWeather.COL_SUNSET_TIME))
                .createTime(row.getLocalDateTime(ForecastWeather.COL_CREATE_TIME))
                .build();
    }

    private static WeatherVO getWeatherVO(RealtimeWeather rtWeather, ForecastWeather fcWeather) {
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
