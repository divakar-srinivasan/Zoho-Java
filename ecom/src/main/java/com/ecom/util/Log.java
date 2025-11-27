package com.ecom.util;

public class Log {
    public static void info(String target,String value){
        System.out.println("\n[########] "+target+"  [ INFO ] ---->  " +value+"   <-----\n");
    }

    public static void err(String target,String value){
        System.out.println("\n[########] "+target+" <--- [ ERROR ] ---->  " +value+"   <-----");
    }
}
