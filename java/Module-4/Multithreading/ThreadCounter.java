package Multithreading;

class SyncCounter{
    int count=0;
    public void increment(){
        count++;
    }
}

public class ThreadCounter {
    public static void main(String args[])throws InterruptedException{
        SyncCounter c=new SyncCounter();
        Thread t1=new Thread(()->{
            for(int i=0;i<2000;i++){
                c.increment();
            }
        });

        Thread t2=new Thread(()->{
            for(int i=0;i<2000;i++){
                c.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final result" + c.count);

    }
}
