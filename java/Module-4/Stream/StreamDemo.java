package Stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.*;

class StreamDemo1{
    public void level1(){
        int a[]={1,2,3,4,5};
        int b[]=Arrays.stream(a).filter(i->i%2==0).toArray();
        System.out.println(Arrays.toString(b));
    }
    public void level2(){
        List<Integer> num= Arrays.asList(1,2,3,4,5);
        List<Integer> list=num.stream().map(i->i*2).collect(Collectors.toList());
        System.out.println(list);
    }
    public void level3(){
        List<Integer> num= Arrays.asList(3,5,2,1,4);
        List<Integer> list=num.stream().sorted().collect(Collectors.toList());
        System.out.println(list);
    }
    public void level4(){
        int a[]={3,4,6,7,4,3};
        int max=Arrays.stream(a).max().getAsInt();
        int min=Arrays.stream(a).min().getAsInt();
        System.out.println("Min : "+min);
        System.out.println("Max : "+max);
    }
}

public class StreamDemo {
    public static void main(String args[]){
        StreamDemo1 sd=new StreamDemo1();
        sd.level1();
        sd.level2();
        sd.level3();
        sd.level4();
    }
}
