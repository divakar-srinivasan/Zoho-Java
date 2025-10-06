
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

class HashSetDemo{
    public void run(){
        HashSet<Integer> set=new HashSet<>();
        set.add(1);set.add(4);set.add(2);set.add(1);set.add(3);
        set.add(100);set.add(-3);set.add(75);
        System.out.println(set);
    }
}

class LinkedHashsetDemo{
    public void run(){
        LinkedHashSet<Integer> set=new LinkedHashSet<>();
        set.add(4);
        set.add(3);
        set.add(5);
        System.out.println(set);
    }
}

class TreeSetDemo{
    public void run(){
        TreeSet<String> set=new TreeSet<>();
        set.add("Bad");
        set.add("Apart");
        set.add("Demo");
        set.add("bad");
        set.add("Apart");
        System.out.println(set);
    }
}

public class SetDemo {
    public static void main(String args[]){
        HashSetDemo hashset=new HashSetDemo();
        LinkedHashsetDemo linkedset=new LinkedHashsetDemo();
        TreeSetDemo treeset=new TreeSetDemo();
        hashset.run();
        linkedset.run();
        treeset.run();
    }
}
