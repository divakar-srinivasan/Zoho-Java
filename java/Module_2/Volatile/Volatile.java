package Volatile;

class SharedData{
    volatile boolean flag=true;
}

class Worker extends Thread{
    SharedData data;
    Worker(SharedData data){
        this.data=data;
    }
    public void run(){
        System.out.println("Work Started");
        while(data.flag){

        }System.out.println("Worker finished");
    }
}

public class Volatile {
    public static void main(String args[]) throws InterruptedException{
        SharedData data=new SharedData();
        Worker worker=new Worker(data);
        worker.start();
        Thread.sleep(2000);
        data.flag=false;
    }
}
