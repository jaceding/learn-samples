package per.jaceding.demo.consumeToProduce;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.*;

/**
 * 消费—转换—生产模式示例
 *
 * @author jaceding
 * @date 2021/5/17
 */
public class TransactionConsumeTransformProduce {

    private static final String BROKER_LIST = "10.69.8.12:9092,10.69.8.13:9092,10.69.8.14:9092";

    private static final String GROUP_ID = "groupId";

    private static final String TRANSACTIONAL_ID = "transactionalId";

    private static final String CONSUMER_TOPIC_NAME = "demo1";

    private static final String PRODUCER_TOPIC_NAME = "demo2";

    private static Properties getConsumerProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        return props;
    }

    private static Properties getProducerProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, TRANSACTIONAL_ID);
        return props;
    }

    public static void main(String[] args) {
        // 初始化消费者
        Consumer<String, String> consumer = new KafkaConsumer<>(getConsumerProperties());
        consumer.subscribe(Collections.singletonList(CONSUMER_TOPIC_NAME));
        // 初始化生产者
        Producer<String, String> producer = new KafkaProducer<>(getProducerProperties());
        // 初始化事务
        producer.initTransactions();

        //noinspection InfiniteLoopStatement
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            if (records.isEmpty()) {
                continue;
            }
            Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>(16, 0.75f);
            // 开启事务
            producer.beginTransaction();

            try {
                for (TopicPartition topicPartition : records.partitions()) {
                    List<ConsumerRecord<String, String>> topicPartitionRecords = records.records(topicPartition);
                    for (ConsumerRecord<String, String> record : topicPartitionRecords) {

                        // 执行一些逻辑操作

                        // 消费-生产模型
                        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(PRODUCER_TOPIC_NAME,
                                record.key(), record.value());
                        producer.send(producerRecord);
                    }
                    long lastConsumedOffset = topicPartitionRecords.get(topicPartitionRecords.size() - 1).offset();
                    offsets.put(topicPartition, new OffsetAndMetadata(lastConsumedOffset + 1));
                }
                // 提交消费位移
                producer.sendOffsetsToTransaction(offsets, GROUP_ID);
                // 提交事务
                producer.commitTransaction();
            } catch (Exception e) {
                // 中止事务
                producer.abortTransaction();
            }
        }
    }
}
