package per.jaceding.demo.helloword;

import io.netty.channel.Channel;
import per.jaceding.demo.helloword.client.NettyTcpClient;

import java.util.concurrent.TimeUnit;

/**
 * 客户端
 *
 * @author jaceding
 * @date 2021/4/2
 */
public class ClientApp {

    public static final String IP = "127.0.0.1";

    public static final int PORT = ServerApp.PORT;

    public static void main(String[] args) throws InterruptedException {
        Channel channel = NettyTcpClient.connect(IP, PORT);
        for (int i = 0; i < 10; i++) {
            channel.writeAndFlush("Hello" + i);
            TimeUnit.SECONDS.sleep(1);
        }
        channel.close();
        NettyTcpClient.shutdown();
    }
}
