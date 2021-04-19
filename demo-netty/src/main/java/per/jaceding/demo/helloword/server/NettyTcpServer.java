package per.jaceding.demo.helloword.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端 TCP Server
 *
 * @author jaceding
 * @date 2020/9/14
 */
@Slf4j
public class NettyTcpServer {

    public static void listen(int port) {
        log.info("TCP Server 绑定端口:{}", port);
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup eventGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, eventGroup)
                    .channel(NioServerSocketChannel.class)
                    // 设置用于握手的队列大小
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 启用心跳，双方TCP套接字建立连接后，在两个小时左右上层没有任何数据传输的情况下，TCP会自动发送一个活动探测数据报文
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 设置日志处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 设置编解码器
                            pipeline.addLast(new StringDecoder(), new StringEncoder());
                            // 设置业务处理器
                            pipeline.addLast(new NettyTcpServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                log.info("TCP Server 启动成功, 端口:{}", port);
                channelFuture.channel().closeFuture().sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("TCP Server 启动失败", e);
        } finally {
            // 释放资源
            log.info("释放资源");
            bossGroup.shutdownGracefully();
            eventGroup.shutdownGracefully();
        }
    }
}
