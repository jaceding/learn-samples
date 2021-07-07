package per.jaceding.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author jaceding
 * @date 2021/5/27
 */
@Slf4j
public class CrudDemo {

    private static final String ZkServer = "10.69.8.12:2181,10.69.8.13:2181,10.69.81.14:2181";
    private static CuratorFramework zkClient = null;

    private static CuratorFramework getClient() {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(5000, 3);
        return CuratorFrameworkFactory.newClient(ZkServer, retryPolicy);
    }

    @BeforeAll
    public static void init() {
        zkClient = getClient();
        zkClient.start();
    }

    @AfterAll
    public static void close() {
        if (zkClient != null) {
            zkClient.close();
        }
    }

    @SuppressWarnings("unused")
    private CuratorFramework getClient(RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
        return CuratorFrameworkFactory.builder()
                .connectString(ZkServer)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }

    /**
     * 创建持久节点
     */
    @Test
    public void create1() throws Exception {
        String path = "/test1";
        String payload = "{\"method\": \"create1\"}";
        String s = zkClient.create()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, payload.getBytes(StandardCharsets.UTF_8));
        log.info(s);
    }

    /**
     * 创建持久顺序节点
     */
    @Test
    public void create2() throws Exception {
        String path = "/test1/node-";
        String payload = "{\"method\": \"create2\"}";
        for (int i = 0; i < 100000; i++) {
            String s = zkClient.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(path, payload.getBytes(StandardCharsets.UTF_8));
            log.info(s);
        }
    }

    /**
     * 创建临时节点
     */
    @Test
    public void create3() throws Exception {
        String path = "/test2";
        String payload = "{\"method\": \"create3\"}";
        String s = zkClient.create()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, payload.getBytes(StandardCharsets.UTF_8));
        log.info(s);
        TimeUnit.SECONDS.sleep(20);
    }

    /**
     * 创建临时顺序节点
     */
    @Test
    public void create4() throws Exception {
        String path = "/test2/node-";
        String payload = "{\"method\": \"create4\"}";
        for (int i = 0; i < 10; i++) {
            String s = zkClient.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(path, payload.getBytes(StandardCharsets.UTF_8));
            log.info(s);
        }
        TimeUnit.SECONDS.sleep(20);
    }

    /**
     * 删除节点
     */
    @Test
    public void delete() throws Exception {
        String path = "/test1";
        zkClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
    }
}
