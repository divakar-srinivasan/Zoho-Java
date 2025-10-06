package AbstractClasses;

abstract class Vechicles{
    abstract void fuel();
    void drive(){
        System.out.println("Drive safe");
    }
}

class Car extends Vechicles{
    void fuel(){
        System.out.println("Fuel low");
    }
}

public class Vechicle {
    public static void main(String args[]){
        Car car=new Car();
        car.fuel();
        car.drive();
    }
}
