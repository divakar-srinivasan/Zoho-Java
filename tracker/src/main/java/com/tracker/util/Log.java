package com.tracker.util;

public class Log {
    public void info(String method,String msg){
        System.out.println("###### "+method+"   <---------------> [ INFO ]-------------  "+msg+"  ------------- [ INFO ] ------------------->");
    }
    public void err(String method, String msg){
            System.out.println("###### "+method+"   <---------------> [ ERROR ]-------------  "+msg+"  ------------- [ ERROR ] ------------------->");
    }
}
