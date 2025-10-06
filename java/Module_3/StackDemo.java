
import java.util.ArrayDeque;
import java.util.Stack;

public class StackDemo {
    public static void main(String args[]){
        Stack<Integer> stack=new Stack<>();

        stack.push(1);
        stack.push(2);
        stack.push(3);

        System.out.println(stack);


        ArrayDeque<Integer> deque=new ArrayDeque<>();
        deque.add(1);
        deque.push(2);
        deque.push(3);
        System.out.println(deque);
    }
}
