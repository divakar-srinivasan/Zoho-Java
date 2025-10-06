import java.util.StringTokenizer;

public class Main {
    public static void main(String args[]){
        String s="Zoho Corp";
        StringTokenizer st=new StringTokenizer(s,"op");
        System.out.println("Tokens:");
        while (st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }
    }
}
