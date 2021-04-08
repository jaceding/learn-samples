package per.jaceding.demo.consumer;

import org.apache.kafka.common.serialization.Deserializer;
import per.jaceding.demo.Message;
import per.jaceding.demo.ProtostuffUtils;

/**
 * 自定义 Message 反序列化器
 *
 * @author jaceding
 * @date 2021/3/2
 */
public class MessageDeserializer implements Deserializer<Message> {

    @Override
    public Message deserialize(String topic, byte[] data) {
        return ProtostuffUtils.deserialize(data, Message.class);
    }
}
