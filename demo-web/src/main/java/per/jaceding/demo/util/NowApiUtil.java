package per.jaceding.demo.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import per.jaceding.demo.entity.ForecastWeather;
import per.jaceding.demo.entity.RealtimeWeather;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NowApi 工具类
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Slf4j
@Component
public class NowApiUtil {
    /**
     * 时间格式
     */
    public static final String PATTERN = "HH:mm";
    /**
     * 常量
     */
    public static final String REALTIME_KEY = "rt";
    public static final String FORECAST_KEY = "fc";

    /**
     * 批量获取实时天气接口
     */
    private static final String BATCH_REALTIME_WEATHER_URL = "http://api.k780.com/?app=weather.realtimeBatch";

    /**
     * 时间字符串格式化器
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    private static String appKey;
    private static String sign;

    /**
     * 批量获取实时天气 接口文档：https://www.nowapi.com/api/weather.realtimeBatch
     *
     * @param abroad 国外地区还是国内地区
     */
    public static Map<String, Object> getBatchRealtimeWeather(boolean abroad) {
        Map<String, Object> params = new HashMap<>(16, 0.75f);
        params.put("app", "weather.realtimeBatch");
        params.put("batchType", abroad ? "gb" : "cn");
        params.put("appkey", appKey);
        params.put("sign", sign);
        params.put("format", "json");
        log.info("getBatchRealtimeWeather URL=[{}] PARAM=[{}]", BATCH_REALTIME_WEATHER_URL, params.toString());
        String result = HttpUtil.get(BATCH_REALTIME_WEATHER_URL, params, 120000);
        log.info("getBatchRealtimeWeather end");
        JSONObject jsonObject = JSONUtil.parseObj(result);
        Integer success = jsonObject.getInt("success");
        if (success == 1) {
            jsonObject = jsonObject.getJSONObject("result");
            Integer dtCount = jsonObject.getInt("dtCount");
            LocalDate date = LocalDate.now();

            List<RealtimeWeather> rwList = new ArrayList<>(dtCount);
            List<ForecastWeather> fcList = new ArrayList<>(dtCount);
            jsonObject = jsonObject.getJSONObject("dtList");
            for (String key : jsonObject.keySet()) {
                JSONObject object = jsonObject.getJSONObject(key);
                rwList.add(RealtimeWeather.builder()
                        .date(date)
                        .wbCityCode(object.getStr("cityid"))
                        .provinceName(object.getStr("area_1"))
                        .cityName(object.getStr("area_2"))
                        .countyName(object.getStr("area_3"))
                        .weatherCode(object.getStr("wtId"))
                        .weather(object.getStr("wtNm"))
                        .temperature(object.getDouble("wtTemp"))
                        .humidity(object.getDouble("wtHumi"))
                        .wdCode(object.getStr("wtWindId"))
                        .wd(object.getStr("wtWindNm"))
                        .wp(object.getDouble("wtWinp"))
                        .ws(object.getDouble("wtWins"))
                        .aqi(object.getDouble("wtAqi"))
                        .visibility(object.getDouble("wtVisibility"))
                        .rainfall(object.getDouble("wtRainfall"))
                        .pressure(object.getDouble("wtPressurel"))
                        .build());
                ForecastWeather forecastWeather = ForecastWeather.builder()
                        .date(date)
                        .wbCityCode(object.getStr("cityid"))
                        .provinceName(object.getStr("area_1"))
                        .cityName(object.getStr("area_2"))
                        .countyName(object.getStr("area_3"))
                        .blueSky(object.getInt("wtBlueSkyId"))
                        .dayWeatherCode(object.getStr("wtId1"))
                        .dayWeather(object.getStr("wtNm1"))
                        .nightWeatherCode(object.getStr("wtId2"))
                        .nightWeather(object.getStr("wtNm2"))
                        .dayTemperature(object.getDouble("wtTemp1"))
                        .nightTemperature(object.getDouble("wtTemp2"))
                        .dayWdCode(object.getStr("wtWindId1"))
                        .dayWd(object.getStr("wtWindNm1"))
                        .nightWdCode(object.getStr("wtWindId2"))
                        .nightWd(object.getStr("wtWindNm2"))
                        .dayWpCode(object.getStr("wtWinpId1"))
                        .dayWp(object.getStr("wtWinpNm1"))
                        .nightWpCode(object.getStr("wtWinpId2"))
                        .nightWp(object.getStr("wtWinpNm2"))
                        .sunriseTime(LocalTime.parse(object.getStr("wtSunr"), TIME_FORMATTER))
                        .sunsetTime(LocalTime.parse(object.getStr("wtSuns"), TIME_FORMATTER))
                        .build();
                if (StrUtil.isNotBlank(object.getStr("wtSunr"))) {
                    forecastWeather.setSunriseTime(LocalTime.parse(object.getStr("wtSunr"), TIME_FORMATTER));
                }
                if (StrUtil.isNotBlank(object.getStr("wtSuns"))) {
                    forecastWeather.setSunsetTime(LocalTime.parse(object.getStr("wtSuns"), TIME_FORMATTER));
                }
                fcList.add(forecastWeather);
            }
            params.clear();
            params.put(REALTIME_KEY, rwList);
            params.put(FORECAST_KEY, fcList);
            return params;
        }
        return null;
    }

    @Value("${system.nowapi.appkey}")
    public void setAppKey(String appKey) {
        NowApiUtil.appKey = appKey;
    }

    @Value("${system.nowapi.sign}")
    public void setSign(String sign) {
        NowApiUtil.sign = sign;
    }
}
