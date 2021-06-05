package per.jaceding.demo.sync;

/**
 * 证明 volatile 的可见性
 *
 * @author jaceding
 * @date 2021/6/4
 */
public class TestVolatile1 {

    /**
     * 如果不加volatile关键字修饰flag变量，程序不会输出“end”
     */
    private static boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (flag) {
                //do sth
            }
            System.out.println("end");
        }, "test").start();

        Thread.sleep(1000);

        flag = false;
    }
}
