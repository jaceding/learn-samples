package per.jaceding.demo.spi.spring;

import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author jaceding
 * @date 2021/5/7
 */
public class SpringSPI {

    public static void main(String[] args) {
        List<Robot> list1 = SpringFactoriesLoader.loadFactories(Robot.class, SpringSPI.class.getClassLoader());
        list1.forEach(Robot::sayHello);
        List<String> list2 = SpringFactoriesLoader.loadFactoryNames(Robot.class, SpringSPI.class.getClassLoader());
        list2.forEach(System.out::println);
    }
}
