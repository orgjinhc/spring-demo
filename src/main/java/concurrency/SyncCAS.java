package concurrency;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class SyncCAS {

    private static final int COUNT = 10;

    private static final CountDownLatch COUNT_DOWN_LATCH_CAS = new CountDownLatch(COUNT);
    private static final CountDownLatch COUNT_DOWN_LATCH_SYNC = new CountDownLatch(COUNT);

    private static final ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(COUNT);

    private static final List LIST = new ArrayList(COUNT);


    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        cas();
        log.info("cas time:{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        sync();
        log.info("sync time:{}", System.currentTimeMillis() - startTime);
    }

    private static void cas() throws InterruptedException {
        Runnable runnable = new Runnable() {
            @SneakyThrows
            public void run() {
                syncForAll();
                COUNT_DOWN_LATCH_CAS.countDown();

            }
        };
        initThread(runnable);
        COUNT_DOWN_LATCH_CAS.await();

    }

    private static void sync() throws InterruptedException {
        Runnable runnable = new Runnable() {
            @SneakyThrows
            public void run() {
                syncForAll();
                COUNT_DOWN_LATCH_SYNC.countDown();
            }
        };
        initThread(runnable);
        COUNT_DOWN_LATCH_SYNC.await();

    }

    private synchronized static void syncForAll() throws InterruptedException {
        Thread.sleep(1000);
    }


    private static void initThread(Runnable runnable) {
        for (int i = 0; i < COUNT; i++) {
            new Thread(runnable, "t" + i).start();
        }
    }
}
