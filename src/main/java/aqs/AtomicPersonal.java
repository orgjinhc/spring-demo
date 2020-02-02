package aqs;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class AtomicPersonal {
    public static void main(String[] args) {
//        ArrayList<Integer> list = new ArrayList<>(10);
//        System.out.println(ClassLayout.parseInstance(list).toPrintable());

        ReentrantLock reentrantLock = new ReentrantLock();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                try {
                    reentrantLock.lockInterruptibly();
                    log.info("thread:{},加锁成功", name);

                    Thread.sleep(5000);
                    reentrantLock.unlock();
                    log.info("thread:{}，释放锁", name);
                    log.info("thread:{},执行成功", name);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error("thread interrupt:{}", name);
                }
            }
        };

        Thread t1 = new Thread(runnable, "t1");
        Thread t2 = new Thread(runnable, "t2");

        try {
            t1.start();
            Thread.sleep(200);

            t2.start();
            Thread.sleep(2000);
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }

        t2.interrupt();

    }


}
