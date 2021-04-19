package per.jaceding.demo.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @author jaceding
 * @date 2021/4/13
 */
public class ChannelTest {

    private static final String FILEPATH = "C:\\Users\\Administrator\\Desktop\\test.txt";

    private static final Charset CHARSET = Charset.forName("UTF-8");

    public static void write() throws IOException {
        FileChannel channel = new RandomAccessFile(FILEPATH, "rw").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        buffer.put("测试 Channel".getBytes(CHARSET));
        buffer.asCharBuffer().put("测试 Channel");
//        buffer.flip();
        channel.write(buffer, 0);
        channel.force(true);
    }

    public static void read() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILEPATH, "rw");) {
            FileChannel channel = file.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int length;
            while ((length = channel.read(byteBuffer)) != -1) {
                byteBuffer.flip();
                System.out.println(new String(byteBuffer.array(), CHARSET));
                byteBuffer.clear();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        write();
        read();
    }
}
