package aqs;


import lock.Mutex;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Question {

    private static int m = 0;

    public static void main(String[] args) {
        final int COUNT = 2;
        Thread[] threads = new Thread[COUNT];

        Mutex mutex = new Mutex();
        for (int i = 0; i < COUNT; i++) {
            threads[i] = new Thread(() -> {
                try {
                    mutex.lock();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    for (int j = 0; j < COUNT; j++) {
//                        //  内存到寄存器:atomic
//                        //  可以被打断
//                        //  寄存器自增：atomic
//                        //  可以被打断
//                        //  写回内存：atomic
//                        m++;
//                    }
                } finally {
                    mutex.unlock();
                }

            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("result:{}", m);

    }
}
