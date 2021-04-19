package per.jaceding.demo.nio;

import java.nio.ByteBuffer;

/**
 * @author jaceding
 * @date 2021/4/14
 */
public class ByteBufferTest {

    public static void main(String[] args) {
        ByteBuffer.allocateDirect(1024);
        ByteBuffer.allocate(1024);
    }
}
