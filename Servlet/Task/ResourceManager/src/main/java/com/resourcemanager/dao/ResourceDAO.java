package com.resourcemanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.resourcemanager.util.DatabaseConnection;
import com.resourcemanager.util.Logger;

public class ResourceDAO {

    public String getResourceDetails(String resourceId) {
        String query = "SELECT id, name FROM resources WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, resourceId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                Logger.info("Resource fetched successfully - ID: " + id + ", Name: " + name);
                return "{\"id\":\"" + id + "\",\"name\":\"" + name + "\"}";
            } else {
                Logger.info("Resource not found - ID: " + resourceId);
                return null;
            }
        } catch (SQLException e) {
            Logger.error("Error fetching resource details for ID: " + resourceId, e);
            return null;
        }
    }

    public String getAllResources() {
        String query = "SELECT id, name FROM resources";
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                if (!first) json.append(",");
                String id = rs.getString("id");
                String name = rs.getString("name");
                json.append("{\"id\":\"").append(id).append("\",\"name\":\"").append(name).append("\"}");
                first = false;
            }
            
            json.append("]");
            Logger.info("All resources fetched successfully");
            return json.toString();
        } catch (SQLException e) {
            Logger.error("Error fetching all resources", e);
            return "[]";
        }
    }
}
