package per.jaceding.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 实时天气VO
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealtimeWeatherVO {

    private String weather;

    private Double temperature;

    private Double humidity;

    private String wd;

    private Double wp;

    private Double ws;

    private Double aqi;

    private Double visibility;

    private Double rainfall;

    private Double pressure;

    private LocalDateTime updateTime;
}
