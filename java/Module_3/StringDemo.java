
class StringBuidlerDemo{
    public void demo(){
        StringBuilder sb = new StringBuilder("A B C");
        sb.append(" D");
        sb.delete(2, 4);
        sb.insert(2, "B ");
        System.out.println(sb);
        sb.reverse();
        System.out.println(sb);



    }
}

class StringDemoExample{
    public void demo(){
        String s1="hello";
        String s2="hello";
        String s3=new String(s1);
        String s4=new String(s2);
        System.out.println(s1==s2);
        System.out.println(s1==s3);
        System.out.println(s2==s4);
    }
}

public class StringDemo {
    public static void main(String args[]){
        StringDemoExample sd=new StringDemoExample();
        sd.demo();
        StringBuidlerDemo sb=new StringBuidlerDemo();
        sb.demo();
    }
}
