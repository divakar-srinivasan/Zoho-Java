package com.tracker.controller;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import com.tracker.service.TaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/task/update/*")
public class UpdateTaskController extends HttpServlet{
    @Override
    protected void doPut(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
        res.setContentType("application/json");
        BufferedReader buffer = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;

        while( (line = buffer.readLine())!=null){
            sb.append(line);
        }
        JSONObject json = new JSONObject(sb.toString());
        String path = req.getPathInfo();
        if(path == null || path.equals("/")){
            res.setStatus(400);
            res.getWriter().write("{\"Invalid URL\" : \"No ID Found\"}");
            return;
        }
        int id = Integer.parseInt(path.substring(1));
        String status = json.getString("status");

        if(status == null){
            res.setStatus(404);
            res.getWriter().write("{\"error\" : \"No body Found\"}");
            return;
        }

        try {
            TaskService service = new TaskService();
            boolean update = service.updateTask(id,status);

            if(!update){
                res.setStatus(404);
                res.getWriter().write("{\"error\":\"No row found to update\"}");
                return;
            }

            res.setStatus(200);
            res.getWriter().write("{\"Update Successfully\" : \""+id+"\"}");
        } catch (Exception e) {
            res.setStatus(500);
            res.getWriter().write("{\"Error\" : \""+e.getMessage()+"\"}");
        }
    }
}
