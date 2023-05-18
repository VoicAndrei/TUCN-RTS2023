import java.util.concurrent.Semaphore;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;



public class TrafficSimulatorGUI extends Application {
    private Intersection intersection = new Intersection();
    private Semaphore semaphore = new Semaphore(1, true); // FIFO Semaphore
    private int rate = 1000; // car generation rate

    private Label northLabel = new Label();
    private Label southLabel = new Label();
    private Label eastLabel = new Label();
    private Label westLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.add(northLabel, 1, 0);
        grid.add(southLabel, 1, 2);
        grid.add(eastLabel, 2, 1);
        grid.add(westLabel, 0, 1);

        primaryStage.setTitle("Traffic Simulator");
        primaryStage.setScene(new Scene(grid, 300, 275));
        primaryStage.show();

        // start the car generation thread
        new Thread(new CarGenerator(intersection, rate)).start();

        // start the intersection threads
        new Thread(new IntersectionThread(intersection, semaphore, intersection.northQueue, "north")).start();
        new Thread(new IntersectionThread(intersection, semaphore, intersection.southQueue, "south")).start();
        new Thread(new IntersectionThread(intersection, semaphore, intersection.eastQueue, "east")).start();
        new Thread(new IntersectionThread(intersection, semaphore, intersection.westQueue, "west")).start();

        // update the labels every second
        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    northLabel.setText("North queue: " + intersection.northQueue.size());
                    southLabel.setText("South queue: " + intersection.southQueue.size());
                    eastLabel.setText("East queue: " + intersection.eastQueue.size());
                    westLabel.setText("West queue: " + intersection.westQueue.size());
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
