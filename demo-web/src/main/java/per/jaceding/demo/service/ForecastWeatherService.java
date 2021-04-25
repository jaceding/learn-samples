package per.jaceding.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.jaceding.demo.entity.ForecastWeather;
import per.jaceding.demo.mapper.ForecastWeatherMapper;

/**
 * 预测天气 业务层
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Service
public class ForecastWeatherService extends ServiceImpl<ForecastWeatherMapper, ForecastWeather> {

    /**
     * 按气象局城市编号查询预测天气
     *
     * @param wbCityCode 气象局城市编号
     * @return 预测天气
     */
    public ForecastWeather getByWbCityCode(String wbCityCode) {
        QueryWrapper<ForecastWeather> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ForecastWeather.COL_WB_CITY_CODE, wbCityCode)
                .orderByDesc(ForecastWeather.COL_ID)
                .last("limit 1");
        return getBaseMapper().selectOne(queryWrapper);
    }
}



