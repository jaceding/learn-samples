package per.jaceding.demo.stampedlock;

/**
 * @author jaceding
 * @date 2021/5/25
 */
public class TestLockOpt {

    public int num = 0;

    public static void main(String[] args) {
        TestLockOpt testLockOpt = new TestLockOpt();
        testLockOpt.run();
        System.out.println(testLockOpt.num);
    }

    public synchronized void test1() {
        num++;
    }

    public void test2() {
        synchronized (TestLockOpt.class) {
            num++;
        }
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            test1();
            test2();
        }
    }
}
