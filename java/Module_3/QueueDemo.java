
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class QueueDemo {
    public static void main(String args[]){
        Queue<Integer> q=new LinkedList<>();

        q.add(1);q.offer(2);q.offer(5);
        q.poll();

        System.out.println(q);

        PriorityQueue<Integer> pq=new PriorityQueue<>((a,b)->b-a);
        pq.add(30);
        pq.add(20);
        pq.add(40);
        pq.add(10);
        System.out.println(pq);
        
    }
}
