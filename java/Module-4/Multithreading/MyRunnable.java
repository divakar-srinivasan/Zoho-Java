package Multithreading;

class Worker implements Runnable{
    private int id;
    Worker(int id){
        this.id=id;
    }
    public void run(){
        try{
            for(int i=0;i<5;i++){
                System.out.println("Worker : "+id);
                Thread.sleep(1000);
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

public class MyRunnable {
    public static void main(String args[])throws InterruptedException{
        Worker w1=new Worker(1);
        Worker w2=new Worker(2);
        Thread t1=new Thread(w1);
        Thread t2=new Thread(w2);
        t1.start();
        t2.start();
    }
}
