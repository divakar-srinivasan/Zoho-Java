package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TestDbServlet", urlPatterns = { "/testdb" })
public class TestDbServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        try (Connection c = DBUtil.getConnection()) {
            resp.getWriter().println("OK - connected to DB");
            resp.getWriter().println("AutoCommit=" + c.getAutoCommit());
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("DB connection failed: " + e.getMessage());
        }
    }
}
