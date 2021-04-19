package per.jaceding.demo.heartbeat.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import per.jaceding.demo.heartbeat.proto.Ping;
import per.jaceding.demo.heartbeat.proto.Pong;

/**
 * @author jaceding
 * @date 2021/4/19
 */
@Slf4j
public class PingHandler extends SimpleChannelInboundHandler<Ping> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Ping msg) throws Exception {
        log.info("ping msg = " + msg.getMsg());
        ctx.writeAndFlush(Pong.builder().msg("pong").build());
    }
}
