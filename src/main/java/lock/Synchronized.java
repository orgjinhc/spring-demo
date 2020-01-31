package lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Synchronized {

    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(3);

    private static final Object LOCK_OBJ = new Object();

    public static void main(String[] args) throws Exception {
        new Thread(new Runnable() {
            public void run() {
                print();
            }
        }, "Thread1").start();

        new Thread(new Runnable() {
            public void run() {
                print();
            }
        }, "Thread2").start();

        new Thread(new Runnable() {
            public void run() {
                print();
            }
        }, "Thread3").start();
        COUNT_DOWN_LATCH.await();
    }

    private static void print() {
        synchronized (LOCK_OBJ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("currentThread : [{}]", Thread.currentThread().getName());
            COUNT_DOWN_LATCH.countDown();
        }
    }

    private static void noLockPrint() {
        log.info("currentThread : [{}]", Thread.currentThread().getName());
        COUNT_DOWN_LATCH.countDown();
    }
}
