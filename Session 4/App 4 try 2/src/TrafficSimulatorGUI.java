import java.util.Queue;
import java.util.concurrent.Semaphore;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TrafficSimulatorGUI extends Application {
    private Intersection intersectionModel = new Intersection();
    private Semaphore semaphore = new Semaphore(1, true); // FIFO Semaphore
    private int rate = 2000; // car generation rate

    private Circle[][] cars = new Circle[4][10];
    private Rectangle[] lights = new Rectangle[4];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        // create the intersection
        Rectangle intersection = new Rectangle(200, 200, 100, 100);
        intersection.setFill(Color.GRAY);
        pane.getChildren().add(intersection);

        // create the cars and lights
        for (int i = 0; i < 4; i++) {
            lights[i] = new Rectangle(i % 2 == 0 ? 250 : (i == 1 ? 150 : 350), i < 2 ? 150 : 350, 10, 10);
            lights[i].setFill(Color.RED);
            pane.getChildren().add(lights[i]);

            for (int j = 0; j < 10; j++) {
                cars[i][j] = new Circle(i % 2 == 0 ? 250 : (i == 1 ? 200 - j * 10 : 300 + j * 10),
                        i < 2 ? 200 - j * 10 : 300 + j * 10, 5);
                cars[i][j].setFill(Color.TRANSPARENT);
                pane.getChildren().add(cars[i][j]);
            }
        }

        primaryStage.setTitle("Traffic Simulator");
        primaryStage.setScene(new Scene(pane, 500, 500));
        primaryStage.show();

        // start the car generation thread
        new Thread(new CarGenerator(intersectionModel, rate)).start();

        // start the intersection threads
        new Thread(new IntersectionThread(intersectionModel, semaphore, intersectionModel.northQueue, 0)).start();
        new Thread(new IntersectionThread(intersectionModel, semaphore, intersectionModel.southQueue, 2)).start();
        new Thread(new IntersectionThread(intersectionModel, semaphore, intersectionModel.eastQueue, 1)).start();
        new Thread(new IntersectionThread(intersectionModel, semaphore, intersectionModel.westQueue, 3)).start();
    }

    class IntersectionThread implements Runnable {
        private Intersection intersection;
        private Semaphore semaphore;
        private Queue<Integer> queue;
        private int direction;

        public IntersectionThread(Intersection intersection, Semaphore semaphore, Queue<Integer> queue, int direction) {
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
                    Platform.runLater(() -> lights[direction].setFill(Color.GREEN));
                    synchronized (intersection) {
                        int carsNumber = Math.min(queue.size(), 10);
                        for (int i = 0; i < carsNumber; i++) {
                            final int carIndex = i;
                            Platform.runLater(() -> cars[direction][carIndex].setFill(Color.TRANSPARENT));
                            queue.poll();
                            Thread.sleep(1000); // 1 second to pass the intersection
                        }
                        for (int i = 0; i < 10; i++) {
                            final int carIndex = i;
                            Platform.runLater(() -> cars[direction][carIndex].setFill(carIndex < queue.size() ? Color.BLACK : Color.TRANSPARENT));
                        }
                    }
                    Platform.runLater(() -> lights[direction].setFill(Color.RED));
                    semaphore.release();
                    Thread.sleep(1000); // wait for 1 second before next attempt
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
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
                        for (int i = 0; i < 4; i++) {
                            intersection.getQueue(i).add(1);
                            for (int j = 0; j < 10; j++) {
                                final int direction = i;
                                final int index = j;
                                Platform.runLater(() -> cars[direction][index].setFill(index < intersection.getQueue(direction).size() ? Color.BLACK : Color.TRANSPARENT));
                            }
                        }
                    }
                    Thread.sleep(rate);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
