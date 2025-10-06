package NestedClass;

class Student{
    static String name="Divakar";

    static class Parent{
    
        private String name;
        Parent(String name){
            this.name=name;
        }
        void display(){
            System.out.println("Parent name : "+name);
            System.out.println("student name "+name);
            
        }
    }
}

public class InnerClass {
    public static void main(String args[]){
        // Student.Parent pt=new Student.Parent("Srinivasan");
        // pt.display();
        System.out.println();

    }
}