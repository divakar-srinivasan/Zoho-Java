package com.backend.controller;

import java.io.IOException;

import com.backend.dao.MessageDao;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/test")
public class test extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException{
        res.setContentType("text/plain");
        MessageDao dao = new MessageDao();
        try{
            int id = 1;
            res.getWriter().write("{\"id\":"+id+"}");
        }catch(Exception e){
            e.printStackTrace();
        }
        

    }
}
