package com.tracker.controller;

import java.io.IOException;
import java.util.List;

import com.tracker.model.TaskModel;
import com.tracker.service.TaskService;
import com.tracker.util.Log;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/getalltask")
public class GetAllTasksController extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);
        res.setContentType("application/json");

        Log log= new Log();
        TaskService service = new TaskService();
        
        String status = req.getParameter("status");
        List<TaskModel> tasks;
        try {
            if(status != null){
                tasks = service.getTasksByStatus(status);
            }
            else{
                tasks=service.getAllTasks();
            }
        StringBuilder json = new StringBuilder();

        json.append("[");
        for(int i=0;i<tasks.size();i++){
            TaskModel t = tasks.get(i);
            json.append("{");
            json.append("\"id\":").append(t.getId()).append(",")
            .append("\"title\":").append("\"").append(t.getTitle()).append("\",")
            .append("\"description\":").append("\"").append(t.getDescription()).append("\",")
            .append("\"status\":").append("\"").append(t.getStatus()).append("\"")
            ;
            json.append("}");
            if(i<tasks.size()-1) json.append(",");
        }
        json.append("]");
        log.info("GET",json.toString());
        res.getWriter().write(json.toString());
        } catch (Exception e) {
            log.err("GET",e.getMessage());
        }

    }
}
