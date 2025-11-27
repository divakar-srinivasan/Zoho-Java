package com.resourcemanager.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.resourcemanager.dao.ResourceDAO;
import com.resourcemanager.util.Logger;

public class ResourceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Parse the URL pattern: /api/v1/resourcemanager/{resource-name}/{resource-id}
            String pathInfo = request.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/")) {
                sendErrorResponse(out, response, 400, "Invalid resource path");
                return;
            }

            String[] parts = pathInfo.trim().split("/");
            
            // parts[0] will be empty due to leading /
            // parts[1] should be resource-name
            // parts[2] should be resource-id
            
            if (parts.length < 3) {
                sendErrorResponse(out, response, 400, "Invalid resource path format");
                return;
            }

            String resourceName = parts[1];
            String resourceId = parts[2];

            Logger.info("Resource API called - Resource: " + resourceName + ", ID: " + resourceId);

            ResourceDAO resourceDAO = new ResourceDAO();
            String resourceJson = resourceDAO.getResourceDetails(resourceId);

            if (resourceJson != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.println(resourceJson);
                Logger.info("Resource API - Success for ID: " + resourceId);
            } else {
                sendErrorResponse(out, response, 404, "Resource not found");
                Logger.info("Resource API - Resource not found for ID: " + resourceId);
            }

        } catch (Exception e) {
            Logger.error("Exception in Resource API", e);
            sendErrorResponse(out, response, 500, "Internal server error: " + e.getMessage());
        } finally {
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String pathInfo = request.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/")) {
                sendErrorResponse(out, response, 400, "Invalid resource path");
                return;
            }

            String[] parts = pathInfo.trim().split("/");
            
            if (parts.length < 2) {
                sendErrorResponse(out, response, 400, "Invalid resource path format");
                return;
            }

            String resourceName = parts[1];
            
            Logger.info("Resource API POST called - Resource: " + resourceName);
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.println("{\"status\":\"success\",\"message\":\"Resource created\"}");

        } catch (Exception e) {
            Logger.error("Exception in Resource API POST", e);
            sendErrorResponse(out, response, 500, "Internal server error: " + e.getMessage());
        } finally {
            out.close();
        }
    }

    private void sendErrorResponse(PrintWriter out, HttpServletResponse response, 
                                   int statusCode, String message) {
        response.setStatus(statusCode);
        out.println("{\"error\":\"" + message + "\"}");
    }
}
