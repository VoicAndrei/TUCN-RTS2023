import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static boolean lockConsumers = false;
    public static void main(String[] args) {
        List<Integer> buffer = new ArrayList<>();
        Lock lock = new ReentrantLock();
        Condition notFull = lock.newCondition();
        Condition notEmpty = lock.newCondition();
        Random random = new Random();

        Thread producer = new Thread(new Producer(buffer, lock, notFull, notEmpty), "Producer");
        Thread consumer1 = new Thread(new Consumer(buffer, lock, notFull, notEmpty), "Consumer 1");
        Thread consumer2 = new Thread(new Consumer(buffer, lock, notFull, notEmpty), "Consumer 2");
        Thread consumer3 = new Thread(new Consumer(buffer, lock, notFull, notEmpty), "Consumer 3");

        producer.start();
        try { Thread.sleep(random.nextInt(100)); } catch (InterruptedException ignored) {}
        consumer1.start();
        try { Thread.sleep(random.nextInt(100)); } catch (InterruptedException ignored) {}
        consumer2.start();
        try { Thread.sleep(random.nextInt(100)); } catch (InterruptedException ignored) {}
        consumer3.start();

        // To test, we can lock the consumers for a while
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                lockConsumers = true;
                System.out.println("Consumers locked");
                Thread.sleep(10000);
                lockConsumers = false;
                System.out.println("Consumers unlocked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

class Producer implements Runnable {
    private final List<Integer> buffer;
    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;

    Producer(List<Integer> buffer, Lock lock, Condition notFull, Condition notEmpty) {
        this.buffer = buffer;
        this.lock = lock;
        this.notFull = notFull;
        this.notEmpty = notEmpty;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                Random random = new Random();
                lock.lock();
                try {
                    while (buffer.size() == 100) {
                        notFull.await();
                    }
                    int num = random.nextInt();
                    buffer.add(num);
                    System.out.println(Thread.currentThread().getName() + " produced " + num);
                    notEmpty.signalAll();
                } finally {
                    lock.unlock();
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final List<Integer> buffer;
    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    private final Random random = new Random();


    Consumer(List<Integer> buffer, Lock lock, Condition notFull, Condition notEmpty) {
        this.buffer = buffer;
        this.lock = lock;
        this.notFull = notFull;
        this.notEmpty = notEmpty;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (Main.lockConsumers) {
                    Thread.sleep(1000);  // sleep when locked
                    continue;
                }
                lock.lock();
                try {
                    while (buffer.isEmpty()) {
                        notEmpty.await();
                    }
                    int num = buffer.remove(0);
                    System.out.println(Thread.currentThread().getName() + " consumed " + num);
                    notFull.signalAll();
                } finally {
                    lock.unlock();
                }
                Thread.sleep(random.nextInt(100));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
