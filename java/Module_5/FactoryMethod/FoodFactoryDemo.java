package FactoryMethod;

interface Food{
    void cook();
}

class Pizza implements Food{
    public void cook(){
        System.out.println("Pizza Ready...");
    }
}
class Briyani implements Food{
    public void cook(){
        System.out.println("Briyani ready...");
    }
}

abstract class FoodFactory{
    abstract Food createFood();
    public void serve(){
        Food f=createFood();
        f.cook();
    }
}

class PizzaFoodFactory extends FoodFactory{
    public Food createFood(){
        return new Pizza();
    }
}

class BriyaniFoodFactory extends FoodFactory{
    public Food createFood(){
        return new Briyani();
    }
}


public class FoodFactoryDemo {
    public static void main(String args[]){
        FoodFactory pizza=new PizzaFoodFactory();
        pizza.serve();
        FoodFactory briyani=new BriyaniFoodFactory();
        briyani.serve();

    }
}
