package per.jaceding.demo.heartbeat;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import per.jaceding.demo.heartbeat.client.NettyTcpClient;
import per.jaceding.demo.heartbeat.proto.Ping;

import java.util.concurrent.TimeUnit;

/**
 * 客户端
 *
 * @author jaceding
 * @date 2021/4/2
 */
@Slf4j
public class ClientApp {

    public static final String IP = "127.0.0.1";

    public static final int PORT = ServerApp.PORT;

    public static void main(String[] args) throws InterruptedException {
        Channel channel = NettyTcpClient.connect(IP, PORT);
        for (int i = 0; i < 10; i++) {
            if (!channel.isActive()) {
                log.info("连接中断, 准备重连");
                channel = NettyTcpClient.connect(IP, PORT);
            }
            channel.writeAndFlush(Ping.builder().msg("ping").build());
            TimeUnit.SECONDS.sleep(2);
        }
        channel.close();
        NettyTcpClient.shutdown();
    }
}
