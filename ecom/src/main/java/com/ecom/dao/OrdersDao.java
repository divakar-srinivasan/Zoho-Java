package com.ecom.dao;

import com.ecom.util.Db;
import com.ecom.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class OrdersDao {
    public boolean insertOrders(String userId,String productName,String type,int price,String priority) throws SQLException{
        String sql = "INSERT INTO ORDERS (user_id,product_name,type,price,priority) values (?,?,?,?,?)";
        try (Connection conn = Db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, userId);
            ps.setString(2, productName);
            ps.setString(3, type);
            ps.setInt(4, price);
            ps.setString(5,priority);
            return (ps.executeUpdate()>0);
        } catch (SQLException e) {
            Log.err("OrdersDao.insertOrders", "SQL Error: " + e.getMessage() + " | SQLState: " + e.getSQLState() + " | ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
            throw new SQLException("Insertion Failed: " + e.getMessage(), e);
        }

        
    }
}
