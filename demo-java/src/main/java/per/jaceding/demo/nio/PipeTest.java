package per.jaceding.demo.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * @author jaceding
 * @date 2021/4/14
 */
public class PipeTest {

    public static void main(String[] args) throws IOException {
        String str = "测试数据";
        Pipe pipe = Pipe.open();

        Pipe.SinkChannel sink = pipe.sink();

        ByteBuffer sinkBuffer = ByteBuffer.allocate(1024);
        sinkBuffer.put(str.getBytes());
        sinkBuffer.flip();

        while (sinkBuffer.hasRemaining()) {
            // 向管道中写入数据
            sink.write(sinkBuffer);
        }

        Pipe.SourceChannel source = pipe.source();
        ByteBuffer sourceBuffer = ByteBuffer.allocate(1024);
        // 从管道中读取数据
        source.read(sourceBuffer);

        System.out.println(new String(sourceBuffer.array()));
    }
}
