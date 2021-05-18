package per.jaceding.demo.consumeToProduce;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 模拟数据 生产者
 *
 * @author jaceding
 * @date 2021/5/17
 */
@Slf4j
public class MockDataProducer {

    private static final String TOPIC_NAME = "demo1";

    private static final String BROKER_LIST = "10.69.8.12:9092,10.69.8.13:9092,10.69.8.14:9092";

    private static Properties initConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }

    public static void main(String[] args) {
        Properties props = initConfig();
        Producer<String, String> producer = new KafkaProducer<>(props);

        String message = "hello:";
        // 用于统计分区消息数
        Map<Integer, Integer> map = new HashMap<>(16, 0.75f);

        //noinspection AlibabaUndefineMagicConstant
        for (int i = 0; i < 100_000_000; i++) {
            producer.send(new ProducerRecord<>(TOPIC_NAME, message + System.currentTimeMillis()), (metadata, e) -> {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    Integer d = map.getOrDefault(metadata.partition(), 0);
                    map.put(metadata.partition(), (d + 1));
                }
            });
        }
        producer.close();
        log.info(map.toString());
    }
}
