import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class Window extends JFrame implements Observer {
    private ArrayList<JProgressBar> bars = new ArrayList<JProgressBar>();

    public Window(int nrThreads) {
        setLayout(null);
        setSize(450, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init(nrThreads);
        this.setVisible(true);
    }

    private void init(int n) {
        for (int i = 0; i < n; i++) {
            JProgressBar pb = new JProgressBar();
            pb.setMaximum(1000);
            pb.setBounds(50, (i + 1) * 30, 350, 20);
            this.add(pb);
            this.bars.add(pb);
        }
    }

    public void update(Observable obs, Object obj) {
        FirModel.FirUpdate update = (FirModel.FirUpdate) obj;
        int id = update.id;
        int val = update.val;
        setProgressValue(id, val);
    }

    public void setProgressValue(int id, int val){
        bars.get(id).setValue(val);
    }
}