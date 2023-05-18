

public class Main {
        private static boolean stopThreads = false;

        public static void main(String[] args){
            FileService service = new FileService("messages.txt");
            RThread reader = new RThread(service);
            RThread reader2 = new RThread(service);
            WThread writer = new WThread(service);


            reader.start();
            writer.start();
            reader2.start();
        }

        public static boolean isStopThreads () {
            return stopThreads;
        }
    }
