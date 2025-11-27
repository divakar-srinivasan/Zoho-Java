package com.backend.worker;

import java.sql.Connection;

import com.backend.dao.MessageDao;
import com.backend.model.MessageModel;
import com.backend.util.DbConnection;
import com.backend.util.Log;

public class MessageWorker implements Runnable{
    private final MessageDao dao = new MessageDao();
    String subTableName;
    public MessageWorker(String subTableName){
        this.subTableName=subTableName;
    }
    
    @Override
    public void run(){
        try(Connection conn = DbConnection.getConnection();) {
            Log.info("Message Worker","Worker Connection Establised");
            while(!Thread.currentThread().isInterrupted()){
            try {
                MessageModel msg = dao.fetchPendingStatus(conn,subTableName);
                if(msg!=null){
                    int id=msg.getId();
                    Log.info("Thread","Processing id :"+id);
                    Thread.sleep(1000);
                    dao.processedMessageAtomic(id, conn, subTableName);
                    Log.info("Thread State","Status:DONE");
                } else {
                    Thread.sleep(5000);
                }
            } catch (InterruptedException ie) {
                Log.info("Thread","Interrupted, shutting down gracefully");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                Log.err("Thread","Error processing message: " + e.getMessage());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        } catch (Exception e) {
            Log.err("Message Worker","Connection err....");
        }
        Log.info("Thread","Worker thread stopped");
    }
}
