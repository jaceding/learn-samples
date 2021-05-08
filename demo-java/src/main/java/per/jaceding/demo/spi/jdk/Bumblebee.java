package per.jaceding.demo.spi.jdk;

/**
 * 大黄蜂
 *
 * @author jaceding
 * @date 2021/5/7
 */
public class Bumblebee implements Robot {
    @Override
    public void sayHello() {
        System.out.println("hello, i am Bumblebee");
    }
}
