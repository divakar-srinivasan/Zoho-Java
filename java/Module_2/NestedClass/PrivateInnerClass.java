package NestedClass;

class PrivateOuter{
    private String oName="Outer";
    private class PrivateInner{
        private String iName="Inner";
        void display(){
            System.out.println("Outer name : "+oName);
            System.out.println("Inner name : "+iName);
        }
    }
    void getInnerClass(){
        PrivateInner in=new PrivateInner();
        in.display();
    }
}

public class PrivateInnerClass {
    public static void main(String args[]){
        PrivateOuter out=new PrivateOuter();
        out.getInnerClass();
    }
}
