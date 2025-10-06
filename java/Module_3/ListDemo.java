
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

class LinkedListDemo{
    public void run(){
        LinkedList<Integer> list=new LinkedList<>();
        list.add(1);list.add(2);list.add(3);
        list.addFirst(0);
        list.addLast(4);
        System.out.println(list);
        list.removeFirst();
        list.removeLast();
        System.out.println(list);
        list.push(4);
        list.push(5);
        list.pop();
        list.offer(5);
        System.out.println(list);
        System.out.println(list.peek());
    }
}
class ArrayListDemo{
    public void demo(){
        ArrayList<String> list=new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        System.out.println(list);
        System.out.println("2nd ELement  : "+list.get(1));
        list.remove(1);
        list.set(1,"D");
        System.out.println(list);
    }
}

public class ListDemo {
    public static void main(String args[]){
        ArrayListDemo al=new ArrayListDemo();
        al.demo();
        LinkedListDemo l=new LinkedListDemo();
        l.run();
    }
}
