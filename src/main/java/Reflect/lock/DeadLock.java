package Reflect.lock;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DeadLock {

    private static Object objectA = new Object();
    private static Object objectB = new Object();
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {

        List list = new ArrayList();
        new Thread(new Runnable() {
            public void run() {
                selfLock(objectA, objectB);
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                selfLock(objectB, objectA);
            }
        }).start();
        countDownLatch.await();
    }

    private static void deadLock1() {
        synchronized (objectA) {
            synchronized (objectB) {
                countDownLatch.countDown();
                System.out.println("deadLock1");
            }
        }
    }

    private static void deadLock2() {
        synchronized (objectB) {
            synchronized (objectA) {
                System.out.println("deadLock2");
            }
        }
    }


    /**
     * 死锁条件：指多个线程因竞争共享资源而造成的一种僵局，若无外力作用，这些线程都将无法向前推进
     *  1.互斥等待
     *  2.循环等待
     *  3.无法剥夺等待
     *  4.hold and wait
     * @param obj1
     * @param obj2
     */
    private static void selfLock(Object obj1, Object obj2) {
        synchronized (obj1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (obj2) {
                countDownLatch.countDown();
                System.out.println("selfLock");
            }
        }
    }
}
