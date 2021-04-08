package per.jaceding.demo.producer;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import per.jaceding.demo.Message;

import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * 自定义 拦截器
 *
 * @author jaceding
 * @date 2021/3/2
 */
public class CustomProducerInterceptor implements ProducerInterceptor<String, Message> {
    private final LongAdder success = new LongAdder();
    private final LongAdder failure = new LongAdder();

    @Override
    public ProducerRecord<String, Message> onSend(ProducerRecord<String, Message> record) {
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            failure.add(1L);
        } else {
            success.add(1L);
        }
    }

    @Override
    public void close() {
        long total = (success.longValue() + failure.longValue());
        double successRatio = success.longValue() / total;
        System.out.println("发送消息数：" + total);
        System.out.println("发送成功率：" + successRatio);
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}
