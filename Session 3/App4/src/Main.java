
public class Main {
    public static void main(String[] args) {
        Integer monitor1 = 0;
        Integer monitor2 = 0;
        new ExecutionThread1(monitor1, monitor2, 7, 2, 3).start();
        new ExecutionThread1(monitor1, 0, 3, 5, 1).start();
        new ExecutionThread1(monitor2, 0, 4, 6, 2).start();
    }
}