package per.jaceding.demo.interrupt;

/**
 * @author jaceding
 * @date 2021/4/6
 */
public class Demo1 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            Long i = 0L;
            while (!Thread.interrupted()) {
                i++;
            }
            System.out.println("Thread interrupt, i = " + i);
        });
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
    }
}
