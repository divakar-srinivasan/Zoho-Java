package Multithreading;

class Exp1Thread extends Thread{
    public void run(){
        for(int i=0;i<5;i++){
            System.out.println("Thread....");
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

class Exp1Runnable implements Runnable{
    public void run(){
        for(int i=0;i<5;i++){
            System.out.println("Runnable....");
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

public class ThreadExample1 {
    public static void main(String args[])throws InterruptedException{
        Exp1Thread t1=new Exp1Thread();
        Thread r1=new Thread(new Exp1Runnable());
        t1.start();
        r1.start();
        
        for(int i=0;i<5;i++){
            System.out.println("Main Thread....");
            Thread.sleep(500);
        }
    }
}
