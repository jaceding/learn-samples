package per.jaceding.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 预测天气 实体
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "forecast_weather")
public class ForecastWeather implements Serializable {
    public static final String COL_ID = "id";
    public static final String COL_DATE = "date";
    public static final String COL_WB_CITY_CODE = "wb_city_code";
    public static final String COL_PROVINCE_NAME = "province_name";
    public static final String COL_CITY_NAME = "city_name";
    public static final String COL_COUNTY_NAME = "county_name";
    public static final String COL_BLUE_SKY = "blue_sky";
    public static final String COL_DAY_WEATHER_CODE = "day_weather_code";
    public static final String COL_DAY_WEATHER = "day_weather";
    public static final String COL_NIGHT_WEATHER_CODE = "night_weather_code";
    public static final String COL_NIGHT_WEATHER = "night_weather";
    public static final String COL_DAY_TEMPERATURE = "day_temperature";
    public static final String COL_NIGHT_TEMPERATURE = "night_temperature";
    public static final String COL_DAY_WD_CODE = "day_wd_code";
    public static final String COL_DAY_WD = "day_wd";
    public static final String COL_NIGHT_WD_CODE = "night_wd_code";
    public static final String COL_NIGHT_WD = "night_wd";
    public static final String COL_DAY_WP_CODE = "day_wp_code";
    public static final String COL_DAY_WP = "day_wp";
    public static final String COL_NIGHT_WP_CODE = "night_wp_code";
    public static final String COL_NIGHT_WP = "night_wp";
    public static final String COL_SUNRISE_TIME = "sunrise_time";
    public static final String COL_SUNSET_TIME = "sunset_time";
    public static final String COL_CREATE_TIME = "create_time";
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 日期
     */
    private LocalDate date;
    /**
     * 气象局城市编号
     */
    private String wbCityCode;
    /**
     * 省/自治区/直辖市/特别行政区
     */
    private String provinceName;
    /**
     * 市/是否城区
     */
    private String cityName;
    /**
     * 区/县/镇/是否城区
     */
    private String countyName;
    /**
     * 是否可见蓝天，1:可见蓝天 0:无蓝天
     */
    private Integer blueSky;
    /**
     * 白天天气编号
     */
    private String dayWeatherCode;
    /**
     * 白天天气
     */
    private String dayWeather;
    /**
     * 夜间天气编号
     */
    private String nightWeatherCode;
    /**
     * 夜间天气
     */
    private String nightWeather;
    /**
     * 白天温度，单位：℃
     */
    private Double dayTemperature;
    /**
     * 夜间温度，单位：℃
     */
    private Double nightTemperature;
    /**
     * 白天风向编号
     */
    private String dayWdCode;
    /**
     * 白天风向
     */
    private String dayWd;
    /**
     * 夜间风向编号
     */
    private String nightWdCode;
    /**
     * 夜间风向
     */
    private String nightWd;
    /**
     * 白天风力编号
     */
    private String dayWpCode;
    /**
     * 白天风力
     */
    private String dayWp;
    /**
     * 夜间风力编号
     */
    private String nightWpCode;
    /**
     * 夜间风力
     */
    private String nightWp;
    /**
     * 日出时间
     */
    private LocalTime sunriseTime;
    /**
     * 日落时间
     */
    private LocalTime sunsetTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}