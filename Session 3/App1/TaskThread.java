package Session3.App1;

public class TaskThread extends Thread {
    Integer sharedResource;

    int minSleep, maxSleep, minActivity, maxActivity;

    public TaskThread(Integer sharedResource, int minSleep, int maxSleep, int minActivity, int maxActivity) {
        this.sharedResource = sharedResource;
        this.minSleep = minSleep;
        this.maxSleep = maxSleep;
        this.minActivity = minActivity;
        this.maxActivity = maxActivity;
    }

    public void run() {
        System.out.println(this.getName() + " - STATE 1");

        synchronized (sharedResource) {
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
            System.out.println("Resource is released");
        }

        System.out.println(this.getName() + " - STATE 3");
    }
}