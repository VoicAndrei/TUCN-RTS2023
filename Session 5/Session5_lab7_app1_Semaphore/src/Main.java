import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Semaphore semaphore1 = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(1);

        while (true) {
            CountDownLatch countDownLatch = new CountDownLatch(3);

            new ExecutionThread(semaphore1, semaphore2, 2, 4, 4, 4, 6, countDownLatch).start();
            new ExecutionThread(semaphore2, semaphore1, 3, 5, 5, 5, 7, countDownLatch).start();

            countDownLatch.countDown();

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class ExecutionThread extends Thread {
    private final Semaphore semaphore1;
    private final Semaphore semaphore2;
    private final int sleep_min;
    private final int sleep_max;
    private final int sleep;
    private final int activity_min;
    private final int activity_max;
    private final CountDownLatch countDownLatch;

    public ExecutionThread(Semaphore semaphore1, Semaphore semaphore2, int sleep_min, int sleep_max, int sleep,
                           int activity_min, int activity_max, CountDownLatch countDownLatch) {
        this.semaphore1 = semaphore1;
        this.semaphore2 = semaphore2;
        this.sleep_min = sleep_min;
        this.sleep_max = sleep_max;
        this.sleep = sleep;
        this.activity_min = activity_min;
        this.activity_max = activity_max;
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        System.out.println(this.getName() + " - STATE 1");

        try {
            Thread.sleep(Math.round(Math.random() * (sleep_max - sleep_min) + sleep_min) * 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        semaphore1.acquireUninterruptibly(1);
        try {
            System.out.println(this.getName() + " - STATE 2");
            int k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
            for (int i = 0; i < k * 100000; i++) {
                i++;
                i--;
            }

            semaphore2.acquireUninterruptibly(1);
            try {
                System.out.println(this.getName() + " - STATE 3");
                try {
                    Thread.sleep(Math.round(sleep) * 500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                semaphore2.release(1);
            }
        } finally {
            semaphore1.release(1);
        }

        System.out.println(this.getName() + " - STATE 4");

        countDownLatch.countDown();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}