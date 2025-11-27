package com.backend.listener;

import com.backend.util.Log;
import com.backend.worker.MessageWorker;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class BackendListener implements ServletContextListener {

    private Thread T1SUB1;
    private Thread T1SUB2;
    private Thread T2SUB1;
    private Thread T2SUB2;
    
    @Override
    public void contextInitialized(ServletContextEvent sce){
        T1SUB1 = new Thread(new MessageWorker("T1SUB1"),"Message Worker T1SUB1");
        T1SUB2 = new Thread(new MessageWorker("T1SUB2"),"Message Worker T1SUB2");
        T2SUB1 = new Thread(new MessageWorker("T2SUB1"),"Message Worker T2SUB1");
        T2SUB2 = new Thread(new MessageWorker("T2SUB2"),"Message Worker T2SUB2");
        T1SUB1.start();
        T1SUB2.start();
        T2SUB1.start();
        T2SUB2.start();
        Log.info("Thread : "+T1SUB1.getName(), "Message Thread Started.....");
        Log.info("Thread : "+T1SUB2.getName(), "Message Thread Started.....");
        Log.info("Thread : "+T2SUB1.getName(), "Message Thread Started.....");
        Log.info("Thread : "+T2SUB2.getName(), "Message Thread Started.....");
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        if(T1SUB1!=null && T1SUB1.isAlive()){
            T1SUB1.interrupt();
        }
                if(T1SUB2!=null && T1SUB2.isAlive()){
            T1SUB2.interrupt();
        }
                if(T2SUB1!=null && T2SUB1.isAlive()){
            T2SUB1.interrupt();
        }
                if(T2SUB2!=null && T2SUB2.isAlive()){
            T2SUB2.interrupt();
        }
    }
}
