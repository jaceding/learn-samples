package per.jaceding.demo.exchanger;

import java.util.concurrent.Exchanger;

/**
 * @author jaceding
 * @date 2021/4/6
 */
public class Demo {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(() -> {
            try {
                String data = Thread.currentThread().getName();
                data = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName() + " = " + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thread1").start();
        new Thread(() -> {
            try {
                String data = Thread.currentThread().getName();
                data = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName() + " = " + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thread2").start();
        new Thread(() -> {
            try {
                String data = Thread.currentThread().getName();
                data = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName() + " = " + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thread3").start();
        new Thread(() -> {
            try {
                String data = Thread.currentThread().getName();
                data = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName() + " = " + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thread4").start();
    }
}
