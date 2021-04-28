package per.jaceding.demo.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * @author jaceding
 * @date 2021/4/7
 */
public class DisruptorDemo {

    public static void main(String[] args) {
        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = () -> new Element();
        // RingBuffer 大小
        int bufferSize = 1024;
        // 消费者的等待策略
        WaitStrategy strategy = new YieldingWaitStrategy();
        // 创建Disruptor
        Disruptor<Element> disruptor = new Disruptor<>(factory, bufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, strategy);

        // 设置消费者 处理Event
        EventHandler<Element> handler1 = (event, sequence, endOfBatch) -> {
            System.out.println("event = " + event.value);
            System.out.println("sequence = " + sequence);
            System.out.println("endOfBatch = " + endOfBatch);
        };
        // 设置消费者 处理Event
        EventHandler<Element> handler2 = (event, sequence, endOfBatch) -> {
            System.out.println("event -> " + event.value);
        };
        disruptor.handleEventsWith(handler1, handler2);
        // 启动Disruptor
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();
        for (int i = 0; ; i++) {
            // 获取下一个可用位置下标
            long sequence = ringBuffer.next();
            try {
                Element s = ringBuffer.get(sequence);
                s.value = "hello " + i;
            } finally {
                ringBuffer.publish(sequence);
            }
            LockSupport.parkNanos(1);
        }
    }

    private static class Element {
        String value;
    }
}
