import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class TrafficSimulator {
    public static void main(String[] args) throws IOException {
        Intersection intersection = new Intersection();
        Semaphore semaphore = new Semaphore(1, true); // FIFO Semaphore

        // read the car generation rate from the configuration file
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\vilkc\\Desktop\\RTS Voic\\Session 4\\App 4\\src\\config.txt"));
        int rate = Integer.parseInt(reader.readLine());
        reader.close();

        // start the car generation thread
        new Thread(new CarGenerator(intersection, rate)).start();

        // start the intersection threads
        new Thread(new IntersectionThread(intersection, semaphore, intersection.northQueue, "north")).start();
        new Thread(new IntersectionThread(intersection, semaphore, intersection.southQueue, "south")).start();
        new Thread(new IntersectionThread(intersection, semaphore, intersection.eastQueue, "east")).start();
        new Thread(new IntersectionThread(intersection, semaphore, intersection.westQueue, "west")).start();

    }
}
