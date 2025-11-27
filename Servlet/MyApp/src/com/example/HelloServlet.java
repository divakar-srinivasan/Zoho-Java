package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<!doctype html><html><head><title>Hello</title></head><body>");
            out.println("<h1>Hello from Tomcat 10 / Jakarta Servlet!</h1>");
            out.println("<p>Request URI: " + req.getRequestURI() + "</p>");
            out.println("</body></html>");
        }
    }
}
