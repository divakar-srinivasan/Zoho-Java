<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%!
    int counter = 0;

    String greet(String name) {
        counter++;
        return "Hello, " + name + "! You are visitor number " + counter + ".";
    }
%>

<html>
<body>
    <h1>Welcome to My Page</h1>

    <%
        String user = "Developer";
    %>

    <p>Welcome <%= user %></p>
    <p><%= greet(user) %></p>
</body>
</html>
