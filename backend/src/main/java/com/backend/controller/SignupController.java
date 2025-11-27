package com.backend.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONObject;

import com.backend.dao.LoginDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/signup")
public class SignupController extends HttpServlet{
    private final LoginDao dao = new LoginDao();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res )throws ServletException,IOException{
        res.setContentType("application/json;charset=UTF-8");
        StringBuilder sb= new StringBuilder();
        try(BufferedReader buffer = req.getReader()){
            String line;
            while((line=buffer.readLine())!=null){
                sb.append(line);
            }
        }catch(Exception e){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"Empty Buffer...");
            return;
        }
        JSONObject json;
        try {
            json = new JSONObject(sb.toString());
        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"Malformed Json in JSON OBJECCT...");
            return;
        }

        String userId = json.optString("user_id",null);
        String userName = json.optString("username",null);
        String password = json.optString("password",null);

        if(userId == null || userName == null || password == null){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"User Id , User name, Password Not found....");
        }

        boolean check;
        try{
            check = dao.Signup(userId, userName, password);
        }catch(SQLException e){
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"DAO ERROR...");
            return;
        }

        if(!check){
            res.sendError(HttpServletResponse.SC_CONFLICT, "Unable to register user");
            return;
        }
        JSONObject resJson = new JSONObject();
        resJson.put("Status","SUCCESS");
        resJson.put("username",userName);
        res.getWriter().write(resJson.toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use POST to signup");
    }
}
