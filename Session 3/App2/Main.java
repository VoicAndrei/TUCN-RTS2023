package Session3.App2;

public class Main {
    public static void main(String[] args) {
        Integer sharedResource1 = Integer.valueOf(1);
        Integer sharedResource2 = Integer.valueOf(1);
        new TaskThread1(sharedResource1, sharedResource2, 4, 4, 2, 4).start();
        new TaskThread1(sharedResource2, sharedResource1, 5, 5, 3, 6).start();
    }
}