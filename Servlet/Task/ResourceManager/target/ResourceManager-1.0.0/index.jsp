<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) session.getAttribute("username");
    if (username != null) {
        response.sendRedirect("home.jsp");
    } else {
        response.sendRedirect("login.jsp");
    }
%>
