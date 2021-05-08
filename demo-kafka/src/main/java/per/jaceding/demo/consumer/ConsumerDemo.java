package per.jaceding.demo.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import per.jaceding.demo.Message;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * Kafka Consumer
 *
 * @author jaceding
 * @date 2021/3/1
 */
public class ConsumerDemo {

    public static final String TOPIC_NAME = "demo_message";

    public static final String GROUP_ID = "demo";

    private static Properties initConfig() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.86.52.74:9092,10.86.52.77:9092,10.86.122.208:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        return props;
    }

    public static void main(String[] args) {
        Properties props = initConfig();
        Consumer<String, Message> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        try {
            while (true) {
                ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, Message> record : records) {
                    System.out.println("topic = " + record.topic() + ", partition = " + record.partition()
                            + ", offset = " + record.offset() + ", key = " + record.key()
                            + ", value = " + record.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
