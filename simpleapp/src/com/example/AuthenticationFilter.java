package com.example;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/home.jsp", "/api/v1/*"})
public class AuthenticationFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("AuthenticationFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        HttpSession session = httpReq.getSession(false); 

        if (session == null || session.getAttribute("username") == null) {
            LOGGER.info("Session invalid or expired for request: " + httpReq.getRequestURI());
            httpResp.sendRedirect(httpReq.getContextPath() + "/index.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        LOGGER.info("User " + username + " accessing: " + httpReq.getRequestURI());

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOGGER.info("AuthenticationFilter destroyed");
    }
}
