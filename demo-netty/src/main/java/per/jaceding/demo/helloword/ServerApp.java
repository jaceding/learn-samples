package per.jaceding.demo.helloword;

import per.jaceding.demo.helloword.server.NettyTcpServer;

/**
 * 服务端
 *
 * @author jaceding
 * @date 2021/4/2
 */
public class ServerApp {

    public static final int PORT = 8080;

    public static void main(String[] args) {
        NettyTcpServer.listen(PORT);
    }
}
