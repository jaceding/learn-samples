package per.jaceding.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jaceding
 * @date 2021/3/4
 */
public class CustomConsumerInterceptor implements ConsumerInterceptor<String, String> {

    public static final long EXPIRE_TIME = 10;

    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        Map<TopicPartition, List<ConsumerRecord<String, String>>> newRecords = new HashMap<>(16);
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<String, String>> tpRecordList = records.records(partition);
            List<ConsumerRecord<String, String>> newTpRecordList = new ArrayList<>();
            for (ConsumerRecord<String, String> record : tpRecordList) {
                if (System.currentTimeMillis() - record.timestamp() <= EXPIRE_TIME) {
                    newTpRecordList.add(record);
                }
            }
            if (!newTpRecordList.isEmpty()) {
                newRecords.put(partition, newTpRecordList);
            }
        }
        return new ConsumerRecords<>(newRecords);
    }

    @Override
    public void onCommit(Map offsets) {
        offsets.forEach((tp, offset) -> {
            System.out.println(tp + ":" + offset);
        });
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}
