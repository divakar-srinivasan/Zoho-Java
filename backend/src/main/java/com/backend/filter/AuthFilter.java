package com.backend.filter;


import java.io.IOException;

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

@WebFilter(urlPatterns={"/insert"})
public class AuthFilter implements Filter{
    @Override
    public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain) throws IOException,ServletException{
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;

        HttpSession session = httpReq.getSession(false);
        if(session != null && session.getAttribute("user_id")!=null){
            chain.doFilter(req,res);
        }else{
            httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpRes.setContentType("application/json");
            httpRes.getWriter().write("{\"error\":\"UNAUTHORIZED\"}");
        }
    }
    @Override
    public void init(FilterConfig config){}

    @Override
    public void destroy(){}
}
