package ObjectPool;

import java.util.*;

class Connection{
    private String id;
    Connection(String id){
        this.id=id;
        System.out.println("Connection Created : "+id);
    }
    public void connect(){
        System.out.println("Using Connection : "+id);
    }
    public String getID(){
        return id;
    }
}

class ConnectionPool {
    private List<Connection> avail = new ArrayList<>();
    private List<Connection> used = new ArrayList<>();
    private static final int MAX_POOL_SIZE=3;

    public ConnectionPool(){
        for(int i=1;i<=MAX_POOL_SIZE;i++){
            avail.add(new Connection("conn"+i));
        }
    }
    public Connection acquire(){
        if(avail.isEmpty()){
            throw new RuntimeException();
        }
        Connection conn = avail.remove(avail.size()-1);
        used.add(conn);
        System.out.println("Acquired : "+conn.getID());
        return conn;
    }

    public void releaseConnection(Connection conn){
        used.remove(conn);
        avail.add(conn);
        System.out.println("Released : "+conn.getID());
    }

}

public class ObjectPoolExample{
    public static void main(String args[]){
        ConnectionPool pool = new ConnectionPool();
        Connection c1=pool.acquire();
        c1.connect();

        Connection c2 = pool.acquire();
        c2.connect();

        pool.releaseConnection(c1);
    }
}