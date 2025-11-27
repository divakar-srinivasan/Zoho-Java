package com.tracker.controller;

import java.io.IOException;

import com.tracker.model.TaskModel;
import com.tracker.service.TaskService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/getbyid/*")
public class GetByIdController extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException{
        res.setContentType("application/json");

        String path = req.getPathInfo();
        if(path == null || path.equals("/")){
            res.getWriter().write("Id not Found");
            return;
        }
        int id = Integer.parseInt(path.substring(1));

        try {
            TaskService service = new TaskService();
            TaskModel task = service.getById(id);

            if(task == null){
                res.getWriter().write("No value found by ID");
                return;
            }

            StringBuilder json = new StringBuilder();
            json.append("{\"id\":").append(task.getId()).append(",\"title\":\"").append(task.getTitle()).append("\",\"description\":\"")
            .append(task.getDescription()).append("\",\"status\":\"").append(task.getStatus()).append("\"}");

            res.getWriter().write(json.toString());

        } catch (Exception e) {
            res.setStatus(500);
            res.getWriter().write("Message : "+e);
        }

    }
    
}
