package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ResourceApiServlet", urlPatterns = {"/api/v1/*"})
public class ResourceApiServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ResourceApiServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo(); 
        resp.setContentType("application/json; charset=UTF-8");
        if (path == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("{\"error\":\"missing path\"}");
            return;
        }
        String[] parts = path.split("/");
        if (parts.length < 4) { 
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("{\"error\":\"invalid path\"}");
            return;
        }
        String applicationName = parts[1];
        String resourceName = parts[2];
        String resourceId = parts[3];

        if (!resourceName.matches("[A-Za-z0-9_]+")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("{\"error\":\"invalid resource name\"}");
            return;
        }

        String sql = "SELECT id, name FROM " + resourceName + " WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            try {
                int idVal = Integer.parseInt(resourceId);
                ps.setInt(1, idVal);
            } catch (NumberFormatException nfe) {
                ps.setString(1, resourceId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String json = "{\"id\":\"" + escapeJson(id) + "\",\"name\":\"" + escapeJson(name) + "\"}";
                    resp.getWriter().println(json);
                    LOGGER.info("Fetched resource: app=" + applicationName + " resource=" + resourceName + " id=" + resourceId);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println("{\"error\":\"not found\"}");
                    LOGGER.info("Resource not found: app=" + applicationName + " resource=" + resourceName + " id=" + resourceId);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB error fetching resource: " + e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("{\"error\":\"db error\"}");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + ex.getMessage(), ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("{\"error\":\"internal error\"}");
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
