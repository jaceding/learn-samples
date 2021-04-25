package per.jaceding.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.jaceding.demo.entity.RealtimeWeather;
import per.jaceding.demo.mapper.RealtimeWeatherMapper;

/**
 * 实时天气业务层
 *
 * @author jaceding
 * @date 2021/4/9
 */
@Service
public class RealtimeWeatherService extends ServiceImpl<RealtimeWeatherMapper, RealtimeWeather> {

    /**
     * 按气象局城市编号查询实时天气
     *
     * @param wbCityCode 气象局城市编号
     * @return 实时天气
     */
    public RealtimeWeather getByWbCityCode(String wbCityCode) {
        QueryWrapper<RealtimeWeather> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(RealtimeWeather.COL_WB_CITY_CODE, wbCityCode)
                .orderByDesc(RealtimeWeather.COL_ID)
                .last("limit 1");
        return getBaseMapper().selectOne(queryWrapper);
    }
}



