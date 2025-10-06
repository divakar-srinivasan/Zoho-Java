package Generics;

class BoxType{

    static <T> void run(T value){
        System.out.println(value);
    }
}

class GenericMethod{

}

public class Box {
    public static void main(String args[]){
        
        BoxType.run("diva");
        BoxType.run(123);

    }
}
