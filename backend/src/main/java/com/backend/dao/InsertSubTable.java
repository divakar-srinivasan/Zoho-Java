package com.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.backend.util.DbConnection;
import com.backend.util.Log;

public class InsertSubTable {
    public String insertSubTable(String userId,String content,String typeName,String priority) throws Exception{
        PartitionDao dao = new PartitionDao();
        String subTablename = dao.getSubTableName(userId, typeName);
        if(subTablename == null){
            Log.err("InsertSubtable","SubTable name not found....!");
            throw new IllegalArgumentException("SubTableName not found...");
        }
        
        String sql = "INSERT INTO "+subTablename+" (user_id,content,priority) values (?,?,?)";
        try(Connection con = DbConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);){
            ps.setString(1, userId);
            ps.setString(2,content);
            ps.setString(3, priority);

            int row = ps.executeUpdate();
            Log.info("InsertSubtable : ",Integer.toString(row) );
            return "{\"Table Name\":\""+subTablename+"\",\"Msg-Status\":\"Queued\",\"info\":\"SUCCESS\"}";
        }
        catch(Exception e){
        return "{\"error\":\""+e.getMessage()+"\"}";
        }
    }
}
