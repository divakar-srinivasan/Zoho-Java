package com.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.backend.util.DbConnection;
import com.backend.util.Log;

public class PartitionDao {
    public String getSubTableName(String userId,String typeName) throws Exception{
        if(userId==null || userId.isEmpty()){
            throw new IllegalArgumentException("Invalid UserId");
        }
        if(typeName==null || typeName.isEmpty()){
            throw new IllegalArgumentException("Invalid typeName");
        }
        String sql = "SELECT table_count FROM typemain WHERE type_name=?";
        try(Connection conn = DbConnection.getConnection();
            PreparedStatement ps =conn.prepareStatement(sql);
        ){
            ps.setString(1, typeName);
            try(ResultSet rs = ps.executeQuery();){
                if(!rs.next()){
                    Log.err("SQL", "Table count not found");
                    return null;
                }
                int tableCount = rs.getInt("table_count");
                Log.info("Table Count",Integer.toString(tableCount));
                if(tableCount<=0){
                    throw new IllegalArgumentException("Table count less than 0");
                }
                int hashCode = Math.abs(userId.hashCode()) % tableCount + 1;
                String subTableName = typeName + "SUB" + Integer.toString(hashCode);
                Log.info("SubTable for "+typeName,subTableName);
                return subTableName;
            }
        }
    }



}
