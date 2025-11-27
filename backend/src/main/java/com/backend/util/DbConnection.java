package com.backend.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbConnection {
    private static final HikariDataSource dataSource;

    static {
        HikariDataSource tmp = null;
        try (InputStream input = DbConnection.class.getClassLoader().getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties not found on classpath (expected in WEB-INF/classes)");
            }

            Class.forName("org.postgresql.Driver");

            Properties prop = new Properties();
            prop.load(input);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(prop.getProperty("db.url"));
            config.setUsername(prop.getProperty("db.username"));
            config.setPassword(prop.getProperty("db.password"));
            config.setMaximumPoolSize(Integer.parseInt(prop.getProperty("db.pool.size", "5")));
            config.setMinimumIdle(Integer.parseInt(prop.getProperty("db.pool.minimumIdle", "1")));
            config.setConnectionTimeout(Long.parseLong(prop.getProperty("db.pool.connectionTimeoutMs", "30000")));
            String leak = prop.getProperty("db.pool.leakDetectionThresholdMs");
            if (leak != null) {
                config.setLeakDetectionThreshold(Long.parseLong(leak));
            }

            config.setPoolName("BackendHikariPool");

            tmp = new HikariDataSource(config);

            Log.info("HikariCP", "Database Initialized successfully (pool=" + tmp.getMaximumPoolSize() + ")");

        } catch (Throwable e) {
            System.err.println("DbConnection: initialization failed - revealing root cause:");
            e.printStackTrace(System.err);
            throw new ExceptionInInitializerError(e);
        }
        dataSource = tmp;
    }

    private DbConnection() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static com.zaxxer.hikari.HikariDataSource getDataSource() {
        return dataSource;
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
