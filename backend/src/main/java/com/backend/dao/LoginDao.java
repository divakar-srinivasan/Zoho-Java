package com.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.backend.util.DbConnection;

public class LoginDao {
    public boolean Signup(String userId,String userName,String password)throws SQLException{
        String sql = "insert into users (user_id,username,password) values(?,?,?)";
        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt(12));
        try(Connection conn = DbConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setString(1, userId);
            ps.setString(2,userName);
            ps.setString(3, hashedPassword);

            if(ps.executeUpdate()>0) return true;
            throw new SQLException("Insertion Failed..");
        }
    }

    public boolean Login(String userId,String password)throws SQLException{
        String sql = "select password from users where user_id=?";
        try(Connection conn = DbConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1,userId);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    String storedPassword = rs.getString("password");
                    return BCrypt.checkpw(password, storedPassword);
                }
            }
        }
        return false;
    }
}
