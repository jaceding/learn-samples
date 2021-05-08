package per.jaceding.demo.spi.jdk;

import java.util.ServiceLoader;

/**
 * Java SPI 测试
 *
 * @author jaceding
 * @date 2021/5/7
 */
public class JavaSPI {

    public static void main(String[] args) {
        ServiceLoader<Robot> serviceLoader = ServiceLoader.load(Robot.class);
        serviceLoader.forEach(Robot::sayHello);
    }
}
