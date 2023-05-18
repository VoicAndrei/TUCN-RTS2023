package Session3.App1;

public class Main {
    public static void main(String[] args) {
        Integer sharedResource1 = Integer.valueOf(1);
        Integer sharedResource2 = Integer.valueOf(1);
        new TaskThread(sharedResource1, 4, 4, 2, 4).start();
        new TaskThread2(sharedResource1, sharedResource2, 3, 3, 3, 6).start();
        new TaskThread(sharedResource2, 5, 5, 2, 5).start();
    }
}