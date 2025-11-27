package com.tracker.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    

    public static Connection getConnection(){

        try {

            Properties props = new Properties();
            InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("application.properties");
            props.load(in);

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url,username,password);

        } catch (Exception e) {
            throw new RuntimeException("Failed to connect Database : ",e);
        }
    }
}
