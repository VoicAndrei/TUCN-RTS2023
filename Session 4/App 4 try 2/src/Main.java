import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.io.*;
import java.util.concurrent.TimeUnit;

class Intersection {
    Queue<Integer> northQueue = new LinkedList<>();
    Queue<Integer> southQueue = new LinkedList<>();
    Queue<Integer> eastQueue = new LinkedList<>();
    Queue<Integer> westQueue = new LinkedList<>();
    public Queue<Integer> getQueue(int index) {
        switch (index) {
            case 0:
                return northQueue;
            case 1:
                return eastQueue;
            case 2:
                return southQueue;
            case 3:
                return westQueue;
            default:
                return null;
        }
    }

}

class CarGenerator implements Runnable {
    private Intersection intersection;
    private int rate;

    public CarGenerator(Intersection intersection, int rate) {
        this.intersection = intersection;
        this.rate = rate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (intersection) {
                    intersection.northQueue.add(1);
                    intersection.southQueue.add(1);
                    intersection.eastQueue.add(1);
                    intersection.westQueue.add(1);
                    System.out.println("Car added to each queue. Queues: " +
                            "North: " + intersection.northQueue.size() +
                            ", South: " + intersection.southQueue.size() +
                            ", East: " + intersection.eastQueue.size() +
                            ", West: " + intersection.westQueue.size());
                }
                Thread.sleep(rate);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class IntersectionThread implements Runnable {
    private Intersection intersection;
    private Semaphore semaphore;
    private Queue<Integer> queue;
    private String direction;

    public IntersectionThread(Intersection intersection, Semaphore semaphore, Queue<Integer> queue, String direction) {
        this.intersection = intersection;
        this.semaphore = semaphore;
        this.queue = queue;
        this.direction = direction;
    }

    @Override
    public void run() {
        try {
            while (true) {
                semaphore.acquire();
                synchronized (intersection) {
                    int cars = Math.min(queue.size(), 10);
                    for (int i = 0; i < cars; i++) {
                        queue.poll();
                        System.out.println("Car passed from " + direction + " direction. Cars left: " + queue.size());
                        Thread.sleep(1000); // 1 second to pass the intersection
                    }
                }
                semaphore.release();
                Thread.sleep(1000); // wait for 1 second before next attempt
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

