
import java.util.Arrays;

class Demo1{
    public void demo(){
        int a[];
        a=new int[5];
        a=new int[6];

        int b[]={1,2,3,4,5};

        int[] c=new int[5];
        for(int i=0;i<5;i++){
            c[i]=i;
        }

        String s[];
        s=new String[5];

        String s1[]={"A","B","C"};

        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(b));
        System.out.println(Arrays.toString(c));
        System.out.println(Arrays.toString(s));
        System.out.println(Arrays.toString(s1));
    }

}

public class ArrayDemo {
    public static void main(String args[]){
        Demo1 d=new Demo1();
        d.demo();
        

    }
}
