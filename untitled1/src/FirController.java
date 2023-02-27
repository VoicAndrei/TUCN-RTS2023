public class FirController {
    private FirModel model;
    private Window view;

    public FirController() {
        model = new FirModel();
        view = new Window(model.getThreads().size());
        model.addObserver(view);
        view.setVisible(true);
    }

    public void startThreads() {
        model.startThreads();
    }

    public void stopThreads() {
        model.stopThreads();
    }

    public static void main(String args[]) {
        FirController controller = new FirController();
        controller.startThreads();
    }
}