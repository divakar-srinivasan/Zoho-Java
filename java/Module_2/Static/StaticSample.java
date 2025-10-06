package Static;

class Counter{
    static String compName="zoho";
    String name;
    Counter(String name){
        this.name=name;
    }
    void display(){
        System.out.println("Name : "+name+" Company : "+compName);
    }

}

class StaticBlock{
    static int data;
    static{
        System.out.println("I am Static block, Runs first while class Loads");
    }
}

public class StaticSample {
    public static void main(String args[]){
        System.out.println("Main Method starts....");
        Counter c1=new Counter("A");
        Counter c2=new Counter("B");
        Counter c3=new Counter("C");
        c1.display();
        c2.display();
        c3.display();
        System.out.println(Counter.compName);

    }
}
