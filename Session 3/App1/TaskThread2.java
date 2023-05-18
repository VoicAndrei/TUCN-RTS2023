package Session3.App1;

public class TaskThread2 extends Thread {
    Integer sharedResource1, sharedResource2;

    int minSleep, maxSleep, minActivity, maxActivity;

    public TaskThread2(Integer sharedResource1, Integer sharedResource2, int minSleep, int maxSleep, int minActivity, int maxActivity) {
        this.sharedResource1 = sharedResource1;
        this.sharedResource2 = sharedResource2;
        this.minSleep = minSleep;
        this.maxSleep = maxSleep;
        this.minActivity = minActivity;
        this.maxActivity = maxActivity;
    }

    public void run() {
        System.out.println(this.getName() + " - STATE 1");

        synchronized (sharedResource1) {
            synchronized (sharedResource2) {
                System.out.println(this.getName() + " - STATE 2");
                int k = (int) Math.round(Math.random() * (maxActivity - minActivity) + minActivity);
                for (int i = 0; i < k * 100000; i++) {
                    i++;
                    i--;
                }
                try {
                    Thread.sleep(Math.round(Math.random() * minSleep) * 500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Resource is released");
        }

        System.out.println(this.getName() + " - STATE 3");
    }
}