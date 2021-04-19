package per.jaceding.demo.heartbeat.proto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * Message 解码器
 *
 * @author jaceding
 * @date 2020/9/9
 */
@Slf4j
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    /**
     * 最小长度
     */
    public static final Integer MIN_LENGTH = 8;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.readableBytes() <= MIN_LENGTH) {
            return;
        }
        // 标记读的位置
        msg.markReaderIndex();
        // 获取length长度的数据类型
        int length = msg.readInt();
        // 获取消息类型
        int messageCode = msg.readInt();
        if (msg.readableBytes() < length) {
            // 如果长度不够，则将readIndex重置到打标记的地方
            msg.resetReaderIndex();
            return;
        }
        // 构造字节数组
        byte[] data = new byte[length];
        Class<?> clazz = MessageEnum.getClassByCode(messageCode);
        if (Objects.nonNull(clazz)) {
            // 将ByteBuf数据复制到字节数组中
            msg.readBytes(data);
            out.add(ProtostuffUtils.deserialize(data, clazz));
        }
    }
}
