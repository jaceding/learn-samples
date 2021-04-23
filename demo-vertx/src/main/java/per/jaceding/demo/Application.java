package per.jaceding.demo;

import cn.hutool.setting.dialect.Props;
import io.vertx.core.AbstractVerticle;

/**
 * 主类
 *
 * @author jaceding
 * @date 2021/4/23
 */
public class Application extends AbstractVerticle {

    public static final Props props = new Props("application.properties");;

    public static void main(String[] args) {

    }
}
