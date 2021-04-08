package per.jaceding.demo.heartbeat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;

/**
 * 客户端 TCP Client
 *
 * @author jaceding
 * @date 2020/9/14
 */
@Slf4j
public class NettyTcpClient {

    private volatile static Bootstrap BOOTSTRAP;

    private static Bootstrap getBootstrap() {
        if (BOOTSTRAP == null) {
            synchronized (NettyTcpClient.class) {
                if (BOOTSTRAP == null) {
                    NioEventLoopGroup group = new NioEventLoopGroup();
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            // 连接超时毫秒数
                            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1500)
                            // 启动TCP_NODELAY代表禁用了Nagle算法，提高实时性，适用于数据传输量比较小的场景
                            .option(ChannelOption.TCP_NODELAY, true)
                            // 设置日志处理器
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    // 设置编解码器
                                    pipeline.addLast(new StringDecoder(), new StringEncoder());
                                    // 设置业务处理器
                                    pipeline.addLast(new NettyTcpClientHandler());
                                }
                            });
                    BOOTSTRAP = bootstrap;
                }
            }
        }
        return BOOTSTRAP;
    }


    public static Channel connect(String serverIp, int serverPort) throws InterruptedException {
        return connect(new InetSocketAddress(serverIp, serverPort));
    }

    public static Channel connect(SocketAddress socketAddress) throws InterruptedException {
        ChannelFuture future = getBootstrap().connect(socketAddress).sync();
        if (future.isSuccess()) {
            log.info("TCP Client 连接成功");
            return future.channel();
        }
        throw new RuntimeException("TCP Client 连接失败");
    }

    public static void shutdown() {
        EventLoopGroup group = BOOTSTRAP.config().group();
        if (Objects.nonNull(group)) {
            log.info("关闭 EventLoopGroup");
            group.shutdownGracefully();
        }
    }
}
