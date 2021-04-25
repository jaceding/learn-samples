package per.jaceding.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@TableName(value = "forecast_weather")
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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 日期
     */
    @TableField(value = "date")
    private LocalDate date;
    /**
     * 气象局城市编号
     */
    @TableField(value = "wb_city_code")
    private String wbCityCode;
    /**
     * 省/自治区/直辖市/特别行政区
     */
    @TableField(value = "province_name")
    private String provinceName;
    /**
     * 市/是否城区
     */
    @TableField(value = "city_name")
    private String cityName;
    /**
     * 区/县/镇/是否城区
     */
    @TableField(value = "county_name")
    private String countyName;
    /**
     * 是否可见蓝天，1:可见蓝天 0:无蓝天
     */
    @TableField(value = "blue_sky")
    private Integer blueSky;
    /**
     * 白天天气编号
     */
    @TableField(value = "day_weather_code")
    private String dayWeatherCode;
    /**
     * 白天天气
     */
    @TableField(value = "day_weather")
    private String dayWeather;
    /**
     * 夜间天气编号
     */
    @TableField(value = "night_weather_code")
    private String nightWeatherCode;
    /**
     * 夜间天气
     */
    @TableField(value = "night_weather")
    private String nightWeather;
    /**
     * 白天温度，单位：℃
     */
    @TableField(value = "day_temperature")
    private Double dayTemperature;
    /**
     * 夜间温度，单位：℃
     */
    @TableField(value = "night_temperature")
    private Double nightTemperature;
    /**
     * 白天风向编号
     */
    @TableField(value = "day_wd_code")
    private String dayWdCode;
    /**
     * 白天风向
     */
    @TableField(value = "day_wd")
    private String dayWd;
    /**
     * 夜间风向编号
     */
    @TableField(value = "night_wd_code")
    private String nightWdCode;
    /**
     * 夜间风向
     */
    @TableField(value = "night_wd")
    private String nightWd;
    /**
     * 白天风力编号
     */
    @TableField(value = "day_wp_code")
    private String dayWpCode;
    /**
     * 白天风力
     */
    @TableField(value = "day_wp")
    private String dayWp;
    /**
     * 夜间风力编号
     */
    @TableField(value = "night_wp_code")
    private String nightWpCode;
    /**
     * 夜间风力
     */
    @TableField(value = "night_wp")
    private String nightWp;
    /**
     * 日出时间
     */
    @TableField(value = "sunrise_time")
    private LocalTime sunriseTime;
    /**
     * 日落时间
     */
    @TableField(value = "sunset_time")
    private LocalTime sunsetTime;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}