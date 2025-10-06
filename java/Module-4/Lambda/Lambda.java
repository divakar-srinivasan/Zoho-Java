package Lambda;


interface LambdaExp{
    void run();
}

public class Lambda {
    public static void main(String args[]){

        LambdaExp le=new LambdaExp(){
            public void run(){
                System.out.println("Runnung...");
            }
        };
        LambdaExp exp=()->System.out.println("Lambda Version...");
        exp.run();
        le.run();
    }
}
