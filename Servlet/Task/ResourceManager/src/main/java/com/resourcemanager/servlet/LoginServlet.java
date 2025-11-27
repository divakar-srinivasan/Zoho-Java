package com.resourcemanager.servlet;

import java.io.IOException;

import com.resourcemanager.dao.UserDAO;
import com.resourcemanager.util.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final long SESSION_TIMEOUT = 5 * 60 * 1000; // 5 minutes in milliseconds

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        
        if (userDAO.validateUser(username, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
            session.setAttribute("userId", userDAO.getUserIdByUsername(username));
            session.setMaxInactiveInterval((int)(SESSION_TIMEOUT / 1000)); // Set session timeout
            
            Logger.info("Session created for user: " + username + " with ID: " + session.getId());
            
            response.sendRedirect("home.jsp");
        } else {
            Logger.info("Login attempt failed for user: " + username);
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
