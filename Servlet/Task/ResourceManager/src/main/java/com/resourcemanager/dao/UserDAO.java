package com.resourcemanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.resourcemanager.util.DatabaseConnection;
import com.resourcemanager.util.Logger;

public class UserDAO {

    public boolean validateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            boolean isValid = rs.next();
            
            if (isValid) {
                Logger.info("User login successful: " + username);
            } else {
                Logger.info("User login failed: " + username + " - Invalid credentials");
            }
            
            return isValid;
        } catch (SQLException e) {
            Logger.error("Database error during user validation for: " + username, e);
            return false;
        }
    }

    public String getUserIdByUsername(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException e) {
            Logger.error("Error getting user ID for: " + username, e);
        }
        
        return null;
    }
}
