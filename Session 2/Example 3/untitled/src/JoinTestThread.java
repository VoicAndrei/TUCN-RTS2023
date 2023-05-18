public class JoinTestThread extends Thread{
    Thread t;
    int s = 0;
    JoinTestThread(String n, Thread t)
    {
        this.setName(n);
        this.t=t;
    }
    public int getS()
    {
        return s;
    }
    public void run()
    {
        System.out.println("Thread "+this.getName()+" has entered the run() method");
        try
        {
            if (t != null)
                t.join();
            System.out.println("Thread "+this.getName()+" executing operation.");
            if(this.getName().equals("Thread 1"))
            {
                for(int i = 2; i <= 50002; i++)
                {
                    if(50002 % i == 0)
                    {
                        s = s + i;
                    }
                }
            }
            if(this.getName().equals("Thread 2"))
            {
                for(int i = 2; i <= 20002; i++)
                {
                    if(20002 % i == 0)
                    {
                        s = s + i;
                    }
                }
            }
            System.out.println("Thread "+this.getName()+" has terminated operation.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
