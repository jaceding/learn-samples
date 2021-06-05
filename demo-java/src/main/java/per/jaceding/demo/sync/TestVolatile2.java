package per.jaceding.demo.sync;

/**
 * 证明重排序确实存在
 *
 * @author jaceding
 * @date 2021/6/4
 */
public class TestVolatile2 {

    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    /**
     * 如果发生了 x = 0, y = 0 的情况，就可以证明指令重排序确实存在
     */
    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            Thread one = new Thread(() -> {
                a = 1;
                x = b;
            });

            Thread other = new Thread(() -> {
                b = 1;
                y = a;
            });
            one.start();
            other.start();
            one.join();
            other.join();

            if (x == 0 && y == 0) {
                String result = "第" + i + "次 (" + x + "," + y + "）";
                System.err.println(result);
                break;
            }
        }
    }
}
