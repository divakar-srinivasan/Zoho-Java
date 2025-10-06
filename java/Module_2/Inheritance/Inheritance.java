package Inheritance;

class GrandParent{
    void myResource(){
        System.out.println("Grand resource");
    }
}

class Parent extends GrandParent{
    void myResource(){
        //super.myResource();
        System.out.println("Parent resource");
    }
}

class Child extends Parent{
    void myResource(){
        //super.myResource();
        System.out.println("My resource");
    }
}

public class Inheritance {
    public static void main(String args[]){
         GrandParent gp = new Child();
         gp.myResource();
         
        //Child child=new Child();
        //child.myResource();
    }
}
