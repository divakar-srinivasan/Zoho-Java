package Interface;


interface Camera{
    default void photo(){
        System.out.println("default Camera");
    }
}
interface Keyboard{
    default void type(){
        System.out.println("default Keyboard Typing");
    }
}
class KeyboardIp{
    void type(){
        System.out.println("default Keyboard Typing");
    }
}
interface GameSpace{
    void speedGame();
}
abstract class Gaming implements GameSpace{
    public void speedGame(){
        System.out.print("Speed Gaming");
    }
}

class Iphone extends Gaming implements Camera,Keyboard{
    void ringing(){
        System.out.println("I am ringing");
    }
    public void photo(){
        System.out.println("Iphone photo");
    }
    public void speedGame(){
        System.out.println("Iphone supports Speed Game");
    }

}
class emp{
    int i;
    String s;
}

public class SmartPhone {
    public static void main(String args[]){
        //Gaming c = new Iphone();
        //c.photo();
        //c.ringing();
        //c.speedGame();

        // Camera.photo();
        // Iphone ip=new Iphone();
        // ip.ringing();
        // ip.type();
        // ip.photo();
        // ip.speedGame();

    }
}
