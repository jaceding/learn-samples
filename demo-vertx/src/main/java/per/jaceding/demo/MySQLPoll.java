package per.jaceding.demo;

import cn.hutool.setting.dialect.Props;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;

/**
 * MySQL 连接池
 *
 * @author jaceding
 * @date 2021/4/25
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class MySQLPoll {

    private static final Props PROPS = new Props("application.properties");

    private static MySQLPool pool;

    public static void initPoll(Vertx vertx) {
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(PROPS.getInt("mysql.port"))
                .setHost(PROPS.getStr("mysql.host"))
                .setDatabase(PROPS.getStr("mysql.db"))
                .setUser(PROPS.getStr("mysql.user"))
                .setPassword(PROPS.getStr("mysql.password"));

        // Pool options
        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

        // Create the pooled client
        pool = MySQLPool.pool(vertx, connectOptions, poolOptions);
    }

    public static MySQLPool getPool() {
        return pool;
    }
}
