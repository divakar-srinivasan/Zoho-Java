package com.backend.controller;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import com.backend.dao.InsertSubTable;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/publish")
public class PublishMessage extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException,IOException{
        res.setContentType("application/json");
        StringBuilder sb= new StringBuilder();
        String line;
        try(BufferedReader buffer = req.getReader();){
            if(buffer==null){
                res.setStatus(400);
                res.getWriter().write("{\"error\":\"No Request Found\"}");
                return;
            }
            while((line=buffer.readLine())!=null){
                sb.append(line);
            }
        JSONObject json = new JSONObject(sb.toString());
        String userId = json.getString("user_id");
        String content = json.getString("content");
        String typeName = json.getString("type");
        String priority= json.getString("priority");

        if(userId==null || content==null || typeName==null || priority==null){
            res.setStatus(400);
            res.getWriter().write("{\"error\":\"Invalid Data found\"}");
            return;
        }
        try {
        InsertSubTable insert = new InsertSubTable();
        String result = insert.insertSubTable(userId, content, typeName, priority);
        JSONObject resJson = new JSONObject();
        resJson.put("User_id", userId);
        resJson.put("Content", content);
        resJson.put("Type",typeName);
        resJson.put("Priority",priority);

        res.getWriter().write("["+resJson.toString()+","+ result +"]");
        } catch (Exception e) {
            res.setStatus(500);
            res.getWriter().write("{\"error\":\""+e.getMessage()+"\"}");
            }
        }
        
    }
}
