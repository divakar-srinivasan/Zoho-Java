package com.ecom.dao;

import com.ecom.model.OrdersModel;
import com.ecom.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PartitionsDao {

    public OrdersModel fetchNewOrders(Connection conn) throws SQLException{
        String sql = "select order_id,user_id,product_name,type,price,status,priority from orders order by user_id asc limit 1 for update skip locked";
        try(PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();){
                if(rs.next()){
                    return new OrdersModel(
                        rs.getInt("order_id"),rs.getString("user_id"),rs.getString("product_name"),rs.getString("type"),rs.getInt("price"),rs.getString("status"),rs.getString("priority")
                    );
                }
            }
        return null;
    }

    public String findSubTable(Connection conn, String userId,String type) throws SQLException{
        String sql = "select table_count from maintypetable where type_name=?";

        try(PreparedStatement ps = conn.prepareStatement(sql);){
                ps.setString(1, type);
                try(ResultSet rs = ps.executeQuery();){
                    if(!rs.next()){
                        throw new SQLException("Error : No table_count found!");
                    }
                int tableCount = rs.getInt("table_count");
                Log.info("FindsubTable : ","Table Count :"+Integer.toString(tableCount));
                int hashCode =( Math.abs(userId.hashCode()) % tableCount)+1;
                Log.info("FindTable","tableName : "+type+"SUB"+Integer.toString(hashCode));
                return type+"SUB"+Integer.toString(hashCode);
                }
            }catch(SQLException e){
                Log.err("FindSubTable",e.getMessage());
            }
        return null;
    }

    public boolean insertSubTable(Connection conn) throws SQLException{
        try{
            conn.setAutoCommit(false);
            OrdersModel model = fetchNewOrders(conn);
            if(model==null){
                throw new SQLException("Null Model found....");
            }
            String tableName = findSubTable(conn, model.getUserID(), model.getType());
            if(tableName == null || tableName.isBlank()){
                throw new SQLException("Table Name not found....");
            }

            String insertSql = "insert into "+tableName+" (order_id,user_id,product_name,type,price,status,priority) values (?,?,?,?,?,?,?)";
            try(PreparedStatement ps = conn.prepareStatement(insertSql)){
                model.setStatus("QUEUED");
                ps.setInt(1,model.getOrderId());
                ps.setString(2,model.getUserID());
                ps.setString(3, model.getProductName());
                ps.setString(4, model.getType());
                ps.setInt(5, model.getPrice());
                ps.setString(6, model.getStatus());
                ps.setString(7, model.getPriority());

                if(ps.executeUpdate()<=0) throw new SQLException("Insertion Failed in SubTable....");
            }
            String deleteSql = "delete from orders where order_id=?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)){
                ps.setInt(1,model.getOrderId());
                if(ps.executeUpdate()<=0) throw new SQLException("Deletion Failed in Orders....");
            }
            conn.commit();
            return true;
        }catch(SQLException e){
            Log.err("[InsertSubtable] SQLExp : ",e.getMessage());
            conn.rollback();
            return false;
        }finally{
            conn.setAutoCommit(true);
        }
    }
}
