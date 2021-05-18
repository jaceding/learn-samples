package per.jaceding.demo.simple.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import per.jaceding.demo.simple.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Kafka Producer
 *
 * @author jaceding
 * @date 2021/3/1
 */
@Slf4j
public class ProducerDemo {

    public static final String TOPIC_NAME = "demo_msg";

    private static Properties initConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.86.52.74:9092,10.86.52.77:9092,10.86.122.208:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class.getName());
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomProducerInterceptor.class.getName());
        return props;
    }

    public static void main(String[] args) throws InterruptedException {
        Properties props = initConfig();
        Producer<String, Message> producer = new KafkaProducer<>(props);
        Message message = Message.builder()
                .type("A")
                .value("aaa")
                .build();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 500; i++) {
            producer.send(new ProducerRecord<>(TOPIC_NAME, message), (metadata, e) -> {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    log.info("topic = " + metadata.topic()
                            + ", partition = " + metadata.partition());
                    Integer d = map.getOrDefault(metadata.partition(), 0);
                    map.put(metadata.partition(), (d + 1));
                }
            });
            if (i % 20 == 0) {
                Thread.sleep(50);
            }
        }
        producer.close();
        log.info(map.toString());
    }
}
