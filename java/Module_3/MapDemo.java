
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.TreeMap;

class HashtableDemo{
    public void run(){
        System.out.println("Hash Table Demo.....");
        Hashtable<Integer,String> table=new Hashtable<>();
        table.put(1,"A");
        table.put(2,"B");
        table.put(3,"C");
        for(int k:table.keySet()){
            System.out.println(k+" : "+table.get(k));
        }

        for(var x:table.entrySet()){
            System.out.println(x.getKey()+" : "+x.getValue());
        }
        for(var x:table.values()){
            System.out.println(x);
        }
    }
}

class HashMapDemo{
    public void run(){
        System.out.println("Hash Map Demo.....");
        HashMap<Integer,String> map=new HashMap<>();
        map.put(1,"A");
        map.put(2,"B");
        map.put(3,"C");
        map.put(null,"D");
        map.put(4,null);
        System.out.println(map);
    }
}

class TreeMapDemo{
    public void run(){
        TreeMap<Integer,String> map=new TreeMap<>();
        map.put(4,"D");
        map.put(1,"A");
        map.put(3,"C");
        map.put(2,"B");
        //map.put(null,"E"); Null pointer Exception
        System.out.println(map);
    }
}

class LinkedHashMapDemo{
    public void run(){
        System.out.println("LinkedHashMap Demo....");
        LinkedHashMap<Integer,String> map=new LinkedHashMap<>();
        map.put(null,"A");
        System.out.println(map);
    }
}

public class MapDemo {
    public static void main(String args[]){
        HashtableDemo ht=new HashtableDemo();
        HashMapDemo map=new HashMapDemo();
        TreeMapDemo tmap=new TreeMapDemo();
        LinkedHashMapDemo hmap=new LinkedHashMapDemo();
        ht.run();
        map.run();
        tmap.run();
        hmap.run();
    }
}
