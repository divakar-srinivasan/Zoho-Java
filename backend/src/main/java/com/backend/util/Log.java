package com.backend.util;

public class Log {
    public static void info(String method,String msg){
        System.out.println("###### [ " + method+" ] <----  [INFO] ----->   "+msg+"   <------->");
    }
    public static void err(String method,String msg){
        System.out.println("###### [ " + method+" ] <----  [ERROR] ----->   "+msg+"   <------->");
    }
}
