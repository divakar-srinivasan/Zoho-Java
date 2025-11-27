package com.tracker.controller;


import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/sampe")
public class sample extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req , HttpServletResponse res) throws IOException{
        System.out.println("------------------------------------------------------------------------------------");
        for(int i=0;i<30;i++){
            System.out.println("####################################################################################");
        }
        res.getWriter().write("GET Request Handled");
    }
}
