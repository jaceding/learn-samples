package per.jaceding.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import per.jaceding.demo.exception.BusinessException;
import per.jaceding.demo.service.WeatherService;
import per.jaceding.demo.vo.WeatherVO;

/**
 * 天气 控制层
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Api(tags = "天气API")
@RestController
@RequestMapping("/wt")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @ApiOperation("查询天气")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "wbCityCode", value = "气象局城市编号", required = true, dataTypeClass = String.class)
    })
    @GetMapping
    public ResponseEntity<WeatherVO> getWeather(@RequestParam(value = "wbCityCode") String wbCityCode) {
        if (!weatherService.checkWbCityCode(wbCityCode)) {
            throw new BusinessException("Invalid wbCityCode:" + wbCityCode);
        }
        return ResponseEntity.ok(weatherService.getByWbCityCode(wbCityCode));
    }

    @GetMapping("hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello world");
    }
}
