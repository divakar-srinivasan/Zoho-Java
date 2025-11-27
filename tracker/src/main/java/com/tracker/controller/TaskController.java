package com.tracker.controller;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import com.tracker.service.TaskService;
import com.tracker.util.Log;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/create")
public class TaskController extends HttpServlet{
    private TaskService taskService = new TaskService();

    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
        BufferedReader reader = req.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        while( (line=reader.readLine())!=null){
            jsonBuilder.append(line);
        }

        JSONObject json = new JSONObject(jsonBuilder.toString());
        String title=json.getString("title");
        String description = json.optString("description","");

        res.setContentType("application/json");
        res.getWriter().write("GET Request Recieved : "+jsonBuilder.toString());

        try {

            int id = taskService.createTask(title, description);
            Log log = new Log();
            JSONObject sendRes = new JSONObject();
            sendRes.put("Message ","Task Created Successfully...");
            sendRes.put("Task Id ",id);
            res.getWriter().write(sendRes.toString());
            log.info("POST",sendRes.toString());

        } catch (Exception e) {
            JSONObject err = new JSONObject();
            err.put("Failed to Cread !!!!",e.getMessage());
            res.getWriter().write(err.toString());
        }

    }
}