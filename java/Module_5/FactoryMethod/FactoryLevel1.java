package FactoryMethod;

interface Shapex{
    void draw();
}

class Circlex implements Shapex{
    public void draw(){
        System.out.println("Drawing Circle");
    }
}
class Rectanglex implements Shapex{
    public void draw(){
        System.out.println("Drawing Rectangle");
    }
}

class ShapeFactory{
    public Shapex getShape(String type){
        if(type.equalsIgnoreCase("CIRCLE")){
            return new Circlex();
        }
        if(type.equalsIgnoreCase("RECTANGLE")){
            return new Rectanglex();
        }

        return null;
    }
}

public class FactoryLevel1 {
    public static void main(String args[]){
        ShapeFactory factory=new ShapeFactory();
        Shapex shape1=factory.getShape("Circle");
        Shapex shape2=factory.getShape("Rectangle");

        shape1.draw();
        shape2.draw();

        
    }
}
