package com.ecom.dao;

import com.ecom.model.OrdersModel;
import com.ecom.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




public class ProcessedDao {

    public OrdersModel fetch(Connection conn, String tableName) throws SQLException{
        String sql = "select order_id,user_id,product_name,type,price,status,priority from "+ tableName +" order by priority asc limit 1 for update skip locked";
        try(PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ){
            if(rs.next()){
                return new OrdersModel(
                    rs.getInt("order_id"),rs.getString("user_id"),rs.getString("product_name"),rs.getString("type"),rs.getInt("price"),rs.getString("status"),rs.getString("priority")
                );
            }
        }
        return null;
    }

    public boolean processOrders(Connection conn,String tableName) throws SQLException{
        try {
            conn.setAutoCommit(false);
            OrdersModel model = fetch(conn, tableName);
            if(model == null) throw new SQLException("Null Model found [ EMPTY SUB TABLE ]...");
            Log.info("Model creation ", model.toString());
            String insertSql = "insert into processed_orders (order_id,user_id,product_name,type,price,status,priority) values (?,?,?,?,?,?,?)";
            try(PreparedStatement ps = conn.prepareStatement(insertSql);){
                model.setStatus("DELIVERED");
                ps.setInt(1, model.getOrderId());
                ps.setString(2, model.getUserID());
                ps.setString(3, model.getProductName());
                ps.setString(4, model.getType());
                ps.setInt(5, model.getPrice());
                ps.setString(6, model.getStatus());
                ps.setString(7, model.getPriority());
                if(ps.executeUpdate() <= 0) throw new SQLException("No Row Inserted...");
                Log.info("Insertion : ","Success");
            }
            String deleteSql = "delete from "+ tableName+ " where order_id=?";
            try(PreparedStatement ps = conn.prepareStatement(deleteSql)){
                ps.setInt(1, model.getOrderId());
                if(ps.executeUpdate() <= 0) throw new SQLException("Deletion Failed....");
                Log.info("Deletion : ","Success");
            }
            conn.commit();;
            return true;
        } catch (SQLException e) {
            Log.err("Processed Dao : ",e.getMessage());
            conn.rollback();
            return false;
        } finally{
            conn.setAutoCommit(true);
        }
    }
}
