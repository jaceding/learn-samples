package per.jaceding.demo.producer;

import org.apache.kafka.common.serialization.Serializer;
import per.jaceding.demo.Message;
import per.jaceding.demo.ProtostuffUtils;

/**
 * 自定义 Message 序列化器
 *
 * @author jaceding
 * @date 2021/3/2
 */
public class MessageSerializer implements Serializer<Message> {

    @Override
    public byte[] serialize(String topic, Message data) {
        return ProtostuffUtils.serialize(data);
    }
}
