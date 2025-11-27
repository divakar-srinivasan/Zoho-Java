package com.backend.controller;

import com.backend.dao.LoginDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import com.backend.util.Log;

import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private LoginDao dao = new LoginDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        StringBuilder sb = new StringBuilder();
        try(BufferedReader buffer = req.getReader()){
            String line;
            while((line = buffer.readLine()) != null) sb.append(line);
        }catch(Exception e){
            Log.err("LoginServlet",e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"invalid Buffer Request");
            return;
        }
        JSONObject json;
        try {
            json = new JSONObject(sb.toString());
        } catch (Exception e) {
            Log.err("Login Servley", e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"Malformed json");
            return;
        }

        String userId = json.optString("user_id",null);
        String userName = json.optString("username",null);
        String password = json.optString("password",null);

        if( userId == null || userName == null || password == null ){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"NULL value found");
        }
        boolean validate;
        try {
            validate = dao.Login(userId, password);
        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Server Error");
            return;
        }
        if(!validate){
            JSONObject jsonErr = new JSONObject();
            jsonErr.put("status","Failed");
            jsonErr.put("message","Invalid Credential");
            res.getWriter().write(jsonErr.toString());
            return;
        }

        HttpSession oldSession = req.getSession(false);
        if(oldSession != null){
            oldSession.invalidate();
        }
        HttpSession session = req.getSession(true);
        session.setAttribute("user_id",userId);
        session.setMaxInactiveInterval(30*60);

        JSONObject jsonRes = new JSONObject();
        jsonRes.put("Status","SUCCESS");
        jsonRes.put("user_name",userName);

        res.setStatus(HttpServletResponse.SC_ACCEPTED);
        res.getWriter().write(jsonRes.toString());

    }

}
