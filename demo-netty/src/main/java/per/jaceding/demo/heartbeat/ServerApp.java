package per.jaceding.demo.heartbeat;

import per.jaceding.demo.heartbeat.server.NettyTcpServer;

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
