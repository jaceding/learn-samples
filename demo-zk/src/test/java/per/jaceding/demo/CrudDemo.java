package per.jaceding.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author jaceding
 * @date 2021/5/27
 */
@Slf4j
public class CrudDemo {

    //    private static final String ZkServer = "10.69.8.12:2181,10.69.8.13:2181,10.69.81.14:2181";
    private static final String ZkServer = "10.69.8.12:2181";

    private static CuratorFramework getClient() {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        return CuratorFrameworkFactory.newClient(ZkServer, retryPolicy);
    }

    private CuratorFramework getClient(RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
        return CuratorFrameworkFactory.builder()
                .connectString(ZkServer)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }

    /**
     * 创建普通节点
     */
    @Test
    public void create() throws Exception {
        try (CuratorFramework client = getClient()) {
            client.start();
            String path = "/demo";
            String payload = "测试一下1";
            String s = client.create().forPath(path, payload.getBytes(StandardCharsets.UTF_8));
            log.info(s);
            log.info(new String(client.getData().forPath(path)));
        }
    }

    /**
     * 创建临时节点
     */
    @Test
    public void createEphemeral() throws Exception {
        try (CuratorFramework client = getClient()) {
            client.start();
            String path = "/demo2";
            String payload = "测试一下2";
            String s = client.create().withMode(CreateMode.EPHEMERAL).forPath(path, payload.getBytes(StandardCharsets.UTF_8));
            log.info(s);
            log.info(new String(client.getData().forPath(path)));
            TimeUnit.SECONDS.sleep(20);
        }
        log.info("close zk session");
        TimeUnit.SECONDS.sleep(20);
    }

    /**
     * 创建临时顺序节点
     */
    @Test
    public void createEphemeralSequential() throws Exception {
        try (CuratorFramework client = getClient()) {
            client.start();
            String path = "/demo1/test";
            String payload = "测试一下3";
            for (int i = 0; i < 10; i++) {
                String s = client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, payload.getBytes(StandardCharsets.UTF_8));
                log.info(s);
            }
            TimeUnit.SECONDS.sleep(20);
        }
    }

    /**
     * 获取数据
     */
    @Test
    public void getData() throws Exception {
        try (CuratorFramework client = getClient()) {
            client.start();
            String path = "/demo";
            String s = new String(client.getData().forPath(path));
            log.info(s);
        }
    }

    @Test
    public void setDate() throws Exception {
        try (CuratorFramework client = getClient()) {
            client.start();
            String path = "/demo1";
            String payload = "abc";
            client.setData().forPath(path, payload.getBytes());
        }
    }

    @Test
    public void testSync() throws Exception {
        try (CuratorFramework client = getClient()) {
            client.start();
            String path = "/demo";
            client.sync().forPath(path);
            String s = new String(client.getData().forPath(path));
            log.info(s);
        }
    }
}
