import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();

        new ExecutionThread(lock1, lock2, 2, 4, 4, 4, 6).start();
        new ExecutionThread(lock2, lock1, 3, 5, 5, 5, 7).start();
    }
}

class ExecutionThread extends Thread {
    ReentrantLock lock1, lock2;
    int sleep_min, sleep_max, sleep, activity_min, activity_max;

    public ExecutionThread(ReentrantLock lock1, ReentrantLock lock2, int sleep_min, int sleep_max, int sleep, int activity_min, int activity_max) {
        this.lock1 = lock1;
        this.lock2 = lock2;
        this.sleep_min = sleep_min;
        this.sleep_max = sleep_max;
        this.sleep = sleep;
        this.activity_min = activity_min;
        this.activity_max = activity_max;
    }

    public void run() {
        System.out.println(this.getName() + " - STATE 1");

        if (lock1.tryLock()) {
            System.out.println(this.getName() + " - STATE 2");

            int k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
            for (int i = 0; i < k * 100000; i++) {
                i++;
                i--;
            }

            if (lock2.tryLock()) {
                System.out.println(this.getName() + " - STATE 3");
                try {
                    Thread.sleep(Math.round(sleep) * 500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock2.unlock();
                }
            } else {
                lock1.unlock();
            }
        }

        System.out.println(this.getName() + " - STATE 4");
    }
}