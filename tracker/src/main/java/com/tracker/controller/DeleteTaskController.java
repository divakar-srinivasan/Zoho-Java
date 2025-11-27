package com.tracker.controller;

import java.io.IOException;

import org.json.JSONObject;

import com.tracker.service.TaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/task/delete/*")
public class DeleteTaskController extends HttpServlet{
    @Override
    protected void doDelete(HttpServletRequest req , HttpServletResponse res) throws ServletException,IOException{
        res.setContentType("application/json");

        String path = req.getPathInfo();
        JSONObject json = new JSONObject();
        if(path == null || path.equals("/")){
            res.setStatus(400);
            json.put("error","Task ID Missing");
            res.getWriter().write(json.toString());
            return;
        }
        int id = Integer.parseInt(path.substring(1));
        try {
            TaskService service = new TaskService();
            boolean delete = service.deleteTask(id);
            if(!delete){
                res.setStatus(404);
                json.put("No row Found to delete","delete");
                res.getWriter().write("{\"No row found to delete\":\""+delete+"\"}");
                return;
            }
            res.setStatus(200);
            res.getWriter().write("{\"Suceessfully Delete....\":\""+delete+"\"}");

            
        } catch (Exception e) {
            res.setStatus(500);
            res.getWriter().write("{\"Message\":\""+e.getMessage()+"\"}");

        }
    }
    
}
