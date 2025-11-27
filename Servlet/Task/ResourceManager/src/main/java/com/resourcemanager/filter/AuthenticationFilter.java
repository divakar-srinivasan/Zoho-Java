package com.resourcemanager.filter;

import java.io.IOException;

import com.resourcemanager.util.Logger;

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

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {
        "/login.jsp",
        "/login",
        "/css",
        "/js",
        "/index.jsp",
        "/api"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Logger.info("Authentication Filter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, 
                         FilterChain filterChain) throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Check if the path is public
        boolean isPublicPath = isPublicPath(path);

        if (isPublicPath) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // Check session
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("username") == null) {
            Logger.info("Session invalid/expired for path: " + path + " - Redirecting to login");
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        Logger.debug("User authenticated: " + username + " - Accessing: " + path);
        
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Logger.info("Authentication Filter destroyed");
    }

    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        return false;
    }
}
