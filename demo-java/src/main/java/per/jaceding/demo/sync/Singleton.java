package per.jaceding.demo.sync;

/**
 * volatile dcl
 *
 * @author jaceding
 * @date 2021/6/7
 */
public class Singleton {

    private volatile static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        Object obj = new Object();
    }
}
