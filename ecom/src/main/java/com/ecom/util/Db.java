package com.ecom.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Db {
    private static HikariDataSource conn;
    static{
        try (InputStream input = Db.class.getClassLoader().getResourceAsStream("application.properties")){
            if(input == null){
                throw new FileNotFoundException("application.properties not found...");
            }
            Properties props = new Properties();
            props.load(input);

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            if(url == null || username == null || password == null){
                throw new IllegalArgumentException(" [ url , username , password ] not found ....");
            }

            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new Exception("Driver class not found..."+e.getMessage());
            }
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(1);
            config.setConnectionTimeout(30000);
            config.setLeakDetectionThreshold(2000);

            config.setPoolName("Hikari-DB");
            conn = new HikariDataSource(config);
            Log.info("DB Connection Success", "Established Successfully...."); 
        } catch (Exception e) {
            Log.err("DB Connection","Connection Failed : "+e.getMessage());
        }
    }

    private Db(){}

    public static Connection getConnection()throws SQLException{
        return conn.getConnection();
    }
    public void closeConnection(){
        if(conn!=null && !conn.isClosed()){
            conn.close();
        }
    }
}
