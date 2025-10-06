package Multithreading;

class YieldWorker extends Thread {
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName()); 
            Thread.yield();
        }
    }
}

public class YieldExample {
    public static void main(String args[]) {
        YieldWorker y1 = new YieldWorker();
        YieldWorker y2 = new YieldWorker();
        YieldWorker y3=new YieldWorker();
        YieldWorker y4=new YieldWorker();

        y1.start();
        y2.start();
        y3.start();
        y4.start();


    }
}
