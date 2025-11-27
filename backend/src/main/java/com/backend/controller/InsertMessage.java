package com.backend.controller;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import com.backend.dao.MessageDao;
import com.backend.util.Log;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/insert")
public class InsertMessage extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req , HttpServletResponse res) throws ServletException,IOException{
        res.setContentType("application/json");
        StringBuilder sb = new StringBuilder();
        String line;
        try(BufferedReader buffer = req.getReader();){
            while((line=buffer.readLine())!=null){
            sb.append(line);
        }
        }catch(Exception e){
            res.getWriter().write("{\"error\":\""+e.getMessage()+"\"}");
            return;
        }
        JSONObject json = new JSONObject(sb.toString());
        String user_id = json.getString("user_id");
        String content = json.getString("content");
        
        // Validate input
        if(user_id == null || user_id.trim().isEmpty() || content == null || content.trim().isEmpty()){
            res.setStatus(400);
            res.getWriter().write("{\"error\":\"user_id and content are required\"}");
            return;
        }

        try {
            MessageDao dao = new MessageDao();
            int id = dao.InsertMessage(user_id, content);
            if(id>0){
            Log.info("GET","Inserted Successfully...");
            res.setStatus(200);
            res.getWriter().write("{\"id\":\""+id+"\",\"user_id\":\""+user_id+"\",\"content\":\""+content+"\"}");
            }else{
                res.setStatus(404);
                res.getWriter().write("{\"error\":\"Insertion Failed\"}");
            }

        } catch (Exception e) {
            res.setStatus(500);
            Log.err("GET",e.getMessage());
            res.getWriter().write("{\"error\":\""+e.getMessage()+"\"}");
        }
    }
    
}
