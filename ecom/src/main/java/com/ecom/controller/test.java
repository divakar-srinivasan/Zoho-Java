package com.ecom.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.ecom.dao.ProcessedDao;
import com.ecom.util.Db;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/test")
public class test extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req , HttpServletResponse res) throws ServletException,IOException{
        res.setContentType("text/plain");

        try(Connection conn = Db.getConnection()){
            ProcessedDao dao = new ProcessedDao();
            boolean check = dao.processOrders(conn, "T1SUB1");
            if(check){
                res.getWriter().write("SUCCESS...");
            }
            else{
                res.getWriter().write("FAILED..");
            }
        }catch(SQLException e){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"Error in FetchnewOrders...."+e.getMessage());
        }

    }
}
