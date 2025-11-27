package com.example;

import java.io.IOException;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name="LogoutServlet", urlPatterns={"/logout"})
public class LogoutServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            String user = (String) session.getAttribute("username");
            session.invalidate();
            LOGGER.info("Logout success for user: " + user);
        } else {
            LOGGER.info("Logout called but no session found");
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
