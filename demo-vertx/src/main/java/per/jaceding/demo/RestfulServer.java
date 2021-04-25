package per.jaceding.demo;

import cn.hutool.setting.dialect.Props;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;
import per.jaceding.demo.handler.WeatherHandler;

/**
 * 主类
 *
 * @author jaceding
 * @date 2021/4/23
 */
@Slf4j
public class RestfulServer extends AbstractVerticle {

    private static final Props PROPS = new Props("application.properties");

    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        Vertx vertx = Vertx.vertx(options);
        vertx.deployVerticle(new RestfulServer());
    }

    @Override
    public void start() throws Exception {
        MySQLPoll.initPoll(vertx);
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        api(router);

        Integer port = PROPS.getInt("server.port");
        vertx.createHttpServer().requestHandler(router).listen(port);
        log.info("listen port : {}", port);
    }

    private void api(Router router) {

        router.get("/wt").blockingHandler(WeatherHandler::getWeather, false);
    }
}
