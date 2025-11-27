package com.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import com.backend.model.MessageModel;
import com.backend.util.DbConnection;
import com.backend.util.Log;

public class MessageDao {
    public int InsertMessage(String user_id,String content) throws Exception {
        String sql = "INSERT INTO MESSAGES (USER_ID,CONTENT) VALUES (?,?)";

        try(Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        ){
            ps.setString(1, user_id);
            ps.setString(2, content);
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                }
                else{
                    throw new Exception("Key Not found");
                }
            }
        }
    }

    public MessageModel fetchPendingStatus(Connection conn,String subTableName) throws SQLException{
        String sql = "SELECT  id,status FROM "+subTableName+" WHERE status='PENDING' ORDER BY id ASC LIMIT 1 FOR UPDATE SKIP LOCKED";

        try(
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();){
                if(rs.next()){
                    return new MessageModel(rs.getInt("id"),rs.getString("status"));
                }
            }
        return null;
    }

    public boolean processedMessageAtomic(int id,Connection conn,String subTableName) throws SQLException{
        Set<String> TABLES_CONTAINS = Set.of("T1SUB1","T1SUB2","T2SUB1","T2SUB2");

        if(subTableName == null || !TABLES_CONTAINS.contains(subTableName)){
            throw new IllegalArgumentException("Sub table not found...");
        }

        String updateSql = "UPDATE "+subTableName+" SET status='DONE' WHERE id=?";
        String insertSql = "INSERT INTO processedmessages (msg_id,user_id,content,status,tablename,msgtype) select id,user_id,content,status,?,? FROM "+ subTableName +" WHERE id=?";
        String deleteSql = "DELETE FROM "+ subTableName +" WHERE id=?";

        try {
            conn.setAutoCommit(false);
            try(PreparedStatement update = conn.prepareStatement(updateSql)){
                update.setInt(1, id);
                if(update.executeUpdate()<=0) throw new SQLException("Update Failed....");
            }

            try(PreparedStatement insert = conn.prepareStatement(insertSql)){
                String msgType = subTableName.substring(0,2);
                insert.setString(1, subTableName);
                insert.setString(2, msgType);
                insert.setInt(3, id);
                if(insert.executeUpdate()<=0) throw new SQLException("Insertion Failed...");
            }

                try(PreparedStatement delete = conn.prepareStatement(deleteSql)){
                delete.setInt(1, id);
                if(delete.executeUpdate()<=0) throw new SQLException("Delete Failed..");
            }
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            Log.err("Processed message",e.getMessage());
            conn.rollback();
            return false;
        }finally{
            conn.setAutoCommit(true);
        }
    }
}
