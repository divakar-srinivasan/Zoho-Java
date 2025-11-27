package com.ecom.workers;

import com.ecom.dao.PartitionsDao;
import com.ecom.util.Db;
import com.ecom.util.Log;
import java.sql.Connection;
import java.sql.SQLException;



public class MainThread implements Runnable {
    private PartitionsDao dao;

    @Override
    public void run() {
        Log.info("Main Thread : " + Thread.currentThread().getName(), "Executing scheduled partition job");
        dao = new PartitionsDao();
        Connection conn = null;
        try {
            conn = Db.getConnection();
            boolean check = dao.insertSubTable(conn);
            if (check) {
                Log.info(Thread.currentThread().getName(), "SUCCESS");
            } else {
                Log.info(Thread.currentThread().getName(), "FAILED");
            }
        } catch (SQLException sqle) {
            Log.err(Thread.currentThread().getName(), "SQL error in insertSubTable: " + sqle.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Log.err(Thread.currentThread().getName(), "Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
