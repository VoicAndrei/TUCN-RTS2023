// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
        static int sum1 = 0;
        public static void main(String[] args) throws InterruptedException {
            JoinTestThread w1 = new JoinTestThread("Thread 1",null);
            JoinTestThread w2 = new JoinTestThread("Thread 2",null);
            w1.start();
            w2.start();
            w1.join();
            w2.join();
            sum1 = sum1 + w1.getS();
            System.out.println(sum1);
            sum1 = sum1 + w2.getS();
            System.out.println(sum1);
        }

    }