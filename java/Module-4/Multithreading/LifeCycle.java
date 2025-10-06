package Multithreading;

class LifeCycleWorker extends Thread{
    public void run(){
        try{
            Thread.sleep(2000);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getState());
    }
}

public class LifeCycle {
    public static void main(String args[])throws InterruptedException{
        System.out.println("....Main thread Started.....");
        LifeCycleWorker w=new LifeCycleWorker();
        System.out.println(w.getState());
        w.start();
        System.out.println(w.getState());
        Thread.sleep(500);
        System.out.println(w.getState());
        w.join();
        System.out.println(w.getState());
        System.out.println("....Main thread completed.....");
    }
}
