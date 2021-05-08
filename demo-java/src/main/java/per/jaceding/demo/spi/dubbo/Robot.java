package per.jaceding.demo.spi.dubbo;

import org.apache.dubbo.common.extension.SPI;

/**
 * 机器人接口
 *
 * @author jaceding
 * @date 2021/5/7
 */
@SPI
public interface Robot {

    /**
     * 打招呼
     */
    void sayHello();
}
