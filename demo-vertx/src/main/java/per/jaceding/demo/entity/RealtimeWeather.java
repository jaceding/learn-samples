package per.jaceding.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 实时天气 实体
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealtimeWeather implements Serializable {
    public static final String COL_ID = "id";
    public static final String COL_DATE = "date";
    public static final String COL_WB_CITY_CODE = "wb_city_code";
    public static final String COL_PROVINCE_NAME = "province_name";
    public static final String COL_CITY_NAME = "city_name";
    public static final String COL_COUNTY_NAME = "county_name";
    public static final String COL_WEATHER_CODE = "weather_code";
    public static final String COL_WEATHER = "weather";
    public static final String COL_TEMPERATURE = "temperature";
    public static final String COL_HUMIDITY = "humidity";
    public static final String COL_WD_CODE = "wd_code";
    public static final String COL_WD = "wd";
    public static final String COL_WP = "wp";
    public static final String COL_WS = "ws";
    public static final String COL_AQI = "aqi";
    public static final String COL_VISIBILITY = "visibility";
    public static final String COL_RAINFALL = "rainfall";
    public static final String COL_PRESSURE = "pressure";
    public static final String COL_CREATE_TIME = "create_time";
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
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
     * 天气编号
     */
    private String weatherCode;
    /**
     * 天气
     */
    private String weather;
    /**
     * 温度，单位：℃
     */
    private Double temperature;
    /**
     * 相对湿度，单位：%
     */
    private Double humidity;
    /**
     * 风向编号
     */
    private String wdCode;
    /**
     * 风向
     */
    private String wd;
    /**
     * 风力，单位：级
     */
    private Double wp;
    /**
     * 风速，单位：km/h
     */
    private Double ws;
    /**
     * pm2.5
     */
    private Double aqi;
    /**
     * 能见度，单位：km
     */
    private Double visibility;
    /**
     * 降雨量，单位：mm
     */
    private Double rainfall;
    /**
     * 气压，单位：hpa
     */
    private Double pressure;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}