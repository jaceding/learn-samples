package per.jaceding.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import per.jaceding.demo.service.WeatherService;
import per.jaceding.demo.vo.WeatherVO;
import reactor.core.publisher.Mono;

/**
 * 天气控制层
 *
 * @author jaceding
 * @date 2021/4/25
 */
@RequestMapping("/wt")
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public Mono<WeatherVO> getWeather(@RequestParam("wbCityCode") String wbCityCode) {
        return weatherService.getWeather(wbCityCode);
    }

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("hello");
    }
}
