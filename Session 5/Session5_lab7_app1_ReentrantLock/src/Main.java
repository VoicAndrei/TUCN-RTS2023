import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        while (true) {
            new ExecutionThread(lock1, lock2, 2, 4, 4, 4, 6, cyclicBarrier).start();
            new ExecutionThread(lock2, lock1, 3, 5, 5, 5, 7, cyclicBarrier).start();
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.getMessage();
            }
            cyclicBarrier.reset();
        }
    }
}


class ExecutionThread extends Thread {
    private final ReentrantLock lock1;
    private final ReentrantLock lock2;
    private final int sleep_min;
    private final int sleep_max;
    private final int sleep;
    private final int activity_min;
    private final int activity_max;
    private final CyclicBarrier cyclicBarrier;

    public ExecutionThread(ReentrantLock lock1, ReentrantLock lock2, int sleep_min, int sleep_max, int sleep,
                           int activity_min, int activity_max, CyclicBarrier cyclicBarrier) {
        this.lock1 = lock1;
        this.lock2 = lock2;
        this.sleep_min = sleep_min;
        this.sleep_max = sleep_max;
        this.sleep = sleep;
        this.activity_min = activity_min;
        this.activity_max = activity_max;
        this.cyclicBarrier = cyclicBarrier;
    }

    public void run() {
        System.out.println(this.getName() + " - STATE 1");

        int k = (int) Math.round(Math.random() * (sleep_max - sleep_min) + sleep_min);
        for (int i = 0; i < k * 100000; i++) {
            i++;
            i--;
        }

        if (lock1.tryLock()) {
            try {
                System.out.println(this.getName() + " - STATE 2");
                k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
                for (int i = 0; i < k * 100000; i++) {
                    i++;
                    i--;
                }

                if (lock2.tryLock()) {
                    try {
                        System.out.println(this.getName() + " - STATE 3");
                        try {
                            Thread.sleep(Math.round(sleep) * 500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } finally {
                        lock2.unlock();
                    }
                }
            } finally {
                lock1.unlock();
            }
        }

        System.out.println(this.getName() + " - STATE 4");

        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}