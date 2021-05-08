package per.jaceding.demo.spi.dubbo;

import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * @author jaceding
 * @date 2021/5/7
 */
public class DubboSPI {

    public static void main(String[] args) {
        ExtensionLoader<Robot> extensionLoader =
                ExtensionLoader.getExtensionLoader(Robot.class);
        Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
        optimusPrime.sayHello();
        Robot bumblebee = extensionLoader.getExtension("bumblebee");
        bumblebee.sayHello();
    }
}
