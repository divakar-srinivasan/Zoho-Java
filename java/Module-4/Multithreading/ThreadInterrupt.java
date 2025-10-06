package Multithreading;

class Downloader extends Thread{
    public void run(){
        try{
            for(int i=0;i<5;i++){
            System.out.println("File "+i+" downloaded...");
            Thread.sleep(1000); 
        }
        System.out.print("Downloaded Successfully...");
    }catch(InterruptedException e){
        System.out.println("Download Failed...");
    }
    }
}

public class ThreadInterrupt {
    public static void main(String args[]) throws InterruptedException{
        Downloader d=new Downloader();
        d.start();
        Thread.sleep(6000);
        d.interrupt();
    }
}
