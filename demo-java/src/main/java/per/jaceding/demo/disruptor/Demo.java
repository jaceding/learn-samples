package per.jaceding.demo.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author jaceding
 * @date 2021/4/8
 */
public class Demo {

    static class LongEvent {
        long value;
    }

    static class LongEventConsumer implements EventHandler<LongEvent> {

        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("event = " + event.value);
            System.out.println("sequence = " + sequence);
            System.out.println("endOfBatch = " + endOfBatch);
        }
    }

    static class LongEventProducer {

        private final RingBuffer<LongEvent> ringBuffer;

        public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
            @Override
            public void translateTo(LongEvent event, long sequence, ByteBuffer byteBuffer) {
                event.value = byteBuffer.getLong(0);
            }
        };

        public void onData(ByteBuffer byteBuffer) {
            ringBuffer.publishEvent(TRANSLATOR, byteBuffer);
        }
    }

    public static void main(String[] args) {
        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<LongEvent> factory = LongEvent::new;
        // RingBuffer 大小，必须是2的次幂
        int bufferSize = 1024;
        // 创建Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, DaemonThreadFactory.INSTANCE);

        // 设置消费者 处理Event
        disruptor.handleEventsWith(new LongEventConsumer());

        // 启动Disruptor，开启所有的线程
        disruptor.start();

        // 获取RingBuffer用于生产数据
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducer producer = new LongEventProducer(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        for (long i = 0; ; i++) {
            byteBuffer.putLong(0, i);
            producer.onData(byteBuffer);
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        }
    }
}
