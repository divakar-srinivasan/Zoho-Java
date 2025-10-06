package FactoryMethod;

interface Transport{
    void delivery();
}

class Truck implements Transport{
    public void delivery(){
        System.out.println("Truck Delivery...");
    }
}
class Ship implements Transport{
    public void delivery(){
        System.out.println("Ship Delivery...");
    }
}

abstract class Logistics {
    public abstract Transport createTransport();

    public void planDelivery(){
        Transport t=createTransport();
        t.delivery();
    }
}
class RoadLogistics extends Logistics{
    public Transport createTransport(){
        return new Truck();
    }
}

class SeaLogistics extends Logistics{
    public Transport createTransport(){
        return new Ship();
    }
}

public class TransportFactory {
    public static void main(String args[]){
        RoadLogistics road=new RoadLogistics();
        road.planDelivery();

        SeaLogistics sea=new SeaLogistics();
        sea.planDelivery();
    }
}
