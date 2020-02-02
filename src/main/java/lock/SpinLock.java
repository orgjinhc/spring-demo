package lock;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableServer.THREAD_POLICY_ID;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.Thread.*;
import static java.util.concurrent.locks.LockSupport.*;

@Slf4j
@Data
public class SpinLock {

    private static Unsafe unsafe = null;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (SpinLock.class.getDeclaredField("state"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private static final BlockingDeque BLOCKING_DEQUE = new LinkedBlockingDeque();
    /**
     * 0:unlock
     * 1:lock
     */
    private volatile int state = 0;

    /**
     * 浪费cpu资源
     */
    public void spinLock() {
        while (!unsafe.compareAndSwapInt(this, valueOffset, state, 1)) {
            //  spin
        }
    }

    /**
     * 时间无法控制
     *
     * @param time
     * @throws InterruptedException
     */
    public void sleepLock(long time) throws InterruptedException {
        while (!unsafe.compareAndSwapInt(this, valueOffset, state, 1)) {
            sleep(time);
        }
    }

    /**
     * yield由cpu控制，控制性太差
     */
    public void yieldLock() {
        while (!unsafe.compareAndSwapInt(this, valueOffset, state, 1)) {
            yield();
        }
    }


    /**
     * park
     */
    public void parkLock() {
        while (!unsafe.compareAndSwapInt(this, valueOffset, 0, 1)) {
            addQueue();
        }
    }

    public void unLock() {
        state = 0;
        Thread thread = (Thread) BLOCKING_DEQUE.poll();
        log.info("thread:{},queue:{}",thread.toString(),BLOCKING_DEQUE.toString());
        unpark(thread);
    }

    private void addQueue() {
        log.info("state:{}",state);
        BLOCKING_DEQUE.add(Thread.currentThread());
        log.info(BLOCKING_DEQUE.toString());
        park();
    }


    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(2);

        SpinLock spinLock = new SpinLock();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                spinLock.parkLock();
                log.info("name:{}", Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                spinLock.unLock();
                countDownLatch.countDown();
            }
        };

        new Thread(runnable, "t1").start();
        new Thread(runnable, "t2").start();
        new Thread(runnable, "t3").start();
        new Thread(runnable, "t4").start();
        new Thread(runnable, "t5").start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
