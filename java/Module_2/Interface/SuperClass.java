package Interface;

class GrandParent{
    int rupees;
    GrandParent(){
        rupees=10;
        System.out.println("I am Grandpa");
    }
}
class Parent extends GrandParent{
    int rupees;
    Parent(){
        rupees=20;
        System.out.println("I am Parent");
    }
}

class Child extends Parent{
    int rupees;
    Child(){
        rupees=30;
        System.out.println("I am child");
    }
}

public class SuperClass {
    public static void main(String args[]){
        Child child=new Child();
        System.out.println(child.rupees);
    }
}
