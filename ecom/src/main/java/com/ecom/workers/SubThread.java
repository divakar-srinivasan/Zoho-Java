package com.ecom.workers;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecom.dao.ProcessedDao;
import com.ecom.util.Db;
import com.ecom.util.Log;

public class SubThread implements Runnable{
    private ProcessedDao dao = new ProcessedDao();
    String tableName;
    public SubThread(String tableName){
        this.tableName=tableName;
    }

    @Override
    public void run(){
        Log.info(Thread.currentThread().getName()," Executing scheduled processing job for " + tableName);
        Connection conn = null;
        try{
            conn = Db.getConnection();
            boolean check = dao.processOrders(conn, tableName);
            if (check) {
                Log.info(Thread.currentThread().getName(), tableName + " : SUCCESS");
            } else {
                Log.info(Thread.currentThread().getName(), tableName + " : FAILED");
            }
        }catch(SQLException sqle){
            Log.err(Thread.currentThread().getName(),"SQL Process Exception: "+sqle.getMessage());
        }finally{
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    Log.err(Thread.currentThread().getName(), "Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
