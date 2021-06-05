package per.jaceding.demo.sync;

/**
 * 证明 volatile 的可见性
 *
 * @author jaceding
 * @date 2021/6/4
 */
public class TestVolatile3 {

    static Student student = new Student();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (student.flag) {
            }
            System.out.println("end");
        }, "server").start();

        Thread.sleep(1000);
        student.flag = false;
    }

    static class Student {
        public boolean flag = true;
    }
}
