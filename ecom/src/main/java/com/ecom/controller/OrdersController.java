package com.ecom.controller;

import com.ecom.dao.OrdersDao;
import com.ecom.util.Log;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONObject;




@WebServlet("/order/insert")
public class OrdersController extends HttpServlet{
    private OrdersDao dao = new OrdersDao();
    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
        res.setContentType("application/json");
        StringBuilder sb = new StringBuilder();
        try(BufferedReader buffer = req.getReader();) {
            String line;
            while(( line = buffer.readLine())!= null) sb.append(line);
        } catch (Exception e) {
            Log.err("OrdersController", "Buffer reading failed: " + e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"Empty Buffer...");
            return;
        }
        JSONObject json;
        try {
            json = new JSONObject(sb.toString());
        } catch (IllegalArgumentException e) {
            Log.err("OrdersController", "JSON parsing failed: " + e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"Malformed JSON Object...");
            return;
        }
        String userId = json.optString("user_id",null);
        String productName = json.optString("product_name",null);
        String type = json.optString("type",null);
        int price = json.optInt("price",0);
        String priority = json.optString("priority",null);

        if(userId == null || productName == null || type == null || price == 0 || priority==null){
            Log.err("OrdersController", "Null value found - userId: " + userId + ", productName: " + productName + 
                    ", type: " + type + ", price: " + price + ", priority: " + priority);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"Null value Found....");
            return;
        }
        boolean check;
        try {
            check=dao.insertOrders(userId, productName, type, price,priority);
        } catch (SQLException e) {
            Log.err("OrdersController", "Database insertion failed: " + e.getMessage());
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Inserion Failed...");
            return;
        }
        if(!check){
            Log.err("OrdersController", "No rows inserted - insertion operation returned false");
            res.sendError(HttpServletResponse.SC_NO_CONTENT,"No value inserted...");
            return;
        }
        Log.info("OrdersController", "Order inserted successfully for user: " + userId);
        res.setStatus(200);
        JSONObject resJson = new JSONObject();
        resJson.put("State","SUCCESS");
        resJson.put("user_id",userId);
        resJson.put("status","QUEUED");
        res.getWriter().write(resJson.toString());
    }
}

