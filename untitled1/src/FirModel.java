import java.util.ArrayList;
import java.util.Observable;

public class FirModel extends Observable {
    private static final int noOfThreads = 6;
    private static final int processorLoad = 1000000;
    private ArrayList<Fir> threads;

    public FirModel() {
        threads = new ArrayList<Fir>();
        for (int i = 0; i < noOfThreads; i++) {
            Fir fir = new Fir(i, i + 2, processorLoad);
            threads.add(fir);
        }
    }

    public void startThreads() {
        for (Fir fir : threads) {
            fir.start();
        }
    }

    public void stopThreads() {
        for (Fir fir : threads) {
            fir.interrupt();
        }
    }

    public ArrayList<Fir> getThreads() {
        return threads;
    }

    private void notifyObservers(int id, int val) {
        setChanged();
        notifyObservers(new FirUpdate(id, val));
    }

    public class FirUpdate {
        int id;
        int val;

        FirUpdate(int id, int val) {
            this.id = id;
            this.val = val;
        }
    }

    private class Fir extends Thread {
        int id;
        int processorLoad;

        Fir(int id, int priority, int procLoad) {
            this.id = id;
            this.processorLoad = procLoad;
            this.setPriority(priority);
        }

        public void run() {
            int c = 0;
            while (c < 1000 && !isInterrupted()) {
                for (int j = 0; j < this.processorLoad && !isInterrupted(); j++) {
                    j++;
                    j--;
                }
                c++;
                notifyObservers(id, c);
            }
        }
    }
}