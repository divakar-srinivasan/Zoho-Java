package NestedClass;

class Outer {
    static class Inner {
        void show() {
            System.out.println("Hello from static inner class");
        }
    }
}

class Math{
    static class Add{
        int a,b;
        Add(int a,int b){
            this.a=a;
            this.b=b;
        }
        void addValue(){
           System.out.println("result = "+(a+b)); 
        }
    }
    
}

public class StaticInnerClass {
    public static void main(String[] args) {
        Outer.Inner in=new Outer.Inner();
        in.show();
        Math.Add add=new Math.Add(2,2);
        add.addValue();        
        
    }
}
