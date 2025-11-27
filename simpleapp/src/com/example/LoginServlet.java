package com.example;

import java.io.IOException;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name="LoginServlet", urlPatterns={"/login"})
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        try {
            if (username == null || username.trim().isEmpty()) {
                LOGGER.info("Login failure: empty username");
                resp.sendRedirect(req.getContextPath() + "/?error=empty");
                return;
            }
            HttpSession session = req.getSession(true);
            session.setAttribute("username", username.trim());
            session.setMaxInactiveInterval(2*60); 
            LOGGER.info("Login success for user: " + username);
            resp.sendRedirect(req.getContextPath() + "/home.jsp");
        } catch (Exception e) {
            LOGGER.severe("Exception during login for user='" + username + "': " + e.getMessage());
            throw e;
        }
    }
}
