package per.jaceding.demo.heartbeat.proto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Message 编码器
 *
 * @author jaceding
 * @date 2020/9/9
 */
public class MessageEncoder extends MessageToByteEncoder<AbstractMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractMessage msg, ByteBuf out) throws Exception {
        byte[] data = ProtostuffUtils.serialize(msg);
        out.writeInt(data.length);
        out.writeInt(MessageEnum.getCodeByClass(msg.getClass()));
        out.writeBytes(data);
    }
}
