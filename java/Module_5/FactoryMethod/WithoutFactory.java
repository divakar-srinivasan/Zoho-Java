package FactoryMethod;

interface Shape{
    void draw();
}

class Circle implements Shape{
    public void draw(){
        System.out.println("Drawing a Circle");
    }
}

class Rectangle implements Shape{
    public void draw(){
        System.out.println("Drawinf a Rectangle");
    }
}

public class WithoutFactory {
    public static void main(String args[]){
        Shape shape1=new Circle();
        Shape shape2=new Rectangle();
    
        shape1.draw();
        shape2.draw();
    }

}
