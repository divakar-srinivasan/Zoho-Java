package SingletonMethod;

class Singleton1{
    private static Singleton1 instance;
    private Singleton1(){
        System.out.println("Instance Created....");
    }
    public static Singleton1 getInstance(){
        if(instance==null){
            instance = new Singleton1();
        }
        return instance;
    }
}

public class Single1 {
    public static void main(String args[]){
        Singleton1 s1= Singleton1.getInstance();
        Singleton1 s2=Singleton1.getInstance();

        System.out.println(s1==s2);
    }
}
