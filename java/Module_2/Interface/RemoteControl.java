package Interface;

interface Control{
    void on();
    void off();
    void run();
}

class TV implements Control{
    public void on(){
        System.out.println("TV is on");
    }
    public void off(){
        System.out.println("TV is Off");
    }
    public void run(){
        System.out.println("TV is running");
    }
}

public class RemoteControl {
    public static void main(String args[]){
        Control tv=new TV();
        tv.on();tv.off();tv.run();
    }
}