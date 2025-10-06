package Multithreading;

class Waiter implements Runnable{
    private String waiterName;
    Waiter(String waitername){
        this.waiterName=waitername;
    }
    public void run(){
        for(int i=1;i<=5;i++){
            System.out.println(waiterName+" Serving table"+i);
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

public class Restaurant {
    public static void main(String args[])throws Exception{
        Waiter w1=new Waiter("waiter 1");
        Waiter w2=new Waiter("Waiter 2");

        Thread t1=new Thread(w1);
        Thread t2=new Thread(w2);

        t1.start();
        t2.start();

        t1.join();
       
        System.out.println("Restaurat closed....");

    }
}
