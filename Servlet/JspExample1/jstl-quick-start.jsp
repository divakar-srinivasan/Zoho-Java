<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>JSTL Quick Start Example</title>
    <style>
        body { font-family: Arial; margin: 30px; }
        .example { margin: 20px 0; padding: 15px; background: #f0f0f0; border-left: 4px solid #0066cc; }
        code { background: #f5f5f5; padding: 2px 6px; border-radius: 3px; }
    </style>
</head>
<body>
    <h1>JSTL Quick Start - Simple Examples</h1>
    
    <!-- Example 1: Variables and Output -->
    <div class="example">
        <h3>Example 1: Variables & Output</h3>
        <c:set var="title" value="JSTL is Awesome!" />
        <c:set var="count" value="5" />
        <p>Title: <c:out value="${title}" /></p>
        <p>Count: <c:out value="${count}" /></p>
    </div>
    
    <!-- Example 2: If Condition -->
    <div class="example">
        <h3>Example 2: Conditional (if)</h3>
        <c:set var="number" value="10" />
        <c:if test="${number > 5}">
            <p style="color: green;">✓ Number (${number}) is greater than 5!</p>
        </c:if>
    </div>
    
    <!-- Example 3: Choose-When-Otherwise -->
    <div class="example">
        <h3>Example 3: Choose-When-Otherwise (if-else)</h3>
        <c:set var="dayOfWeek" value="3" />
        <p>
            <c:choose>
                <c:when test="${dayOfWeek == 1}">Monday</c:when>
                <c:when test="${dayOfWeek == 2}">Tuesday</c:when>
                <c:when test="${dayOfWeek == 3}">Wednesday</c:when>
                <c:when test="${dayOfWeek == 4}">Thursday</c:when>
                <c:when test="${dayOfWeek == 5}">Friday</c:when>
                <c:otherwise>Weekend</c:otherwise>
            </c:choose>
        </p>
    </div>
    
    <!-- Example 4: forEach Loop -->
    <div class="example">
        <h3>Example 4: Loop (forEach)</h3>
        <p>Numbers 1 to 5:</p>
        <ul>
            <c:forEach var="i" begin="1" end="5">
                <li>Number: ${i}</li>
            </c:forEach>
        </ul>
    </div>
    
    <!-- Example 5: forEach with Items -->
    <div class="example">
        <h3>Example 5: Loop through List Items</h3>
        <p>Programming Languages:</p>
        <ol>
            <c:forEach var="lang" items="Java,Python,JavaScript,C++,Go">
                <li>${lang}</li>
            </c:forEach>
        </ol>
    </div>
    
    <!-- Example 6: Date Formatting -->
    <div class="example">
        <h3>Example 6: Date & Time Formatting</h3>
        <c:set var="now" value="<%= new java.util.Date() %>" />
        <p>Today's Date: <fmt:formatDate value="${now}" type="date" dateStyle="long" /></p>
        <p>Current Time: <fmt:formatDate value="${now}" type="time" timeStyle="long" /></p>
        <p>Custom Format: <fmt:formatDate value="${now}" pattern="EEEE, MMMM dd, yyyy" /></p>
    </div>
    
    <!-- Example 7: Number Formatting -->
    <div class="example">
        <h3>Example 7: Number Formatting</h3>
        <c:set var="salary" value="5000.5678" />
        <p>Raw Value: ${salary}</p>
        <p>As Currency: <fmt:formatNumber value="${salary}" type="currency" /></p>
        <p>As Percentage: <fmt:formatNumber value="0.75" type="percent" /></p>
        <p>2 Decimals: <fmt:formatNumber value="${salary}" maxFractionDigits="2" /></p>
    </div>
    
    <!-- Example 8: String Functions -->
    <div class="example">
        <h3>Example 8: String Functions</h3>
        <%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
        <c:set var="text" value="Learning JSTL Tags" />
        <ul>
            <li>Original: ${text}</li>
            <li>Length: ${fn:length(text)}</li>
            <li>Uppercase: ${fn:toUpperCase(text)}</li>
            <li>Lowercase: ${fn:toLowerCase(text)}</li>
            <li>Contains "JSTL": ${fn:contains(text, 'JSTL')}</li>
            <li>Starts with "Learning": ${fn:startsWith(text, 'Learning')}</li>
            <li>First 8 chars: ${fn:substring(text, 0, 8)}</li>
        </ul>
    </div>
    
    <!-- Example 9: Empty Check -->
    <div class="example">
        <h3>Example 9: Check if Empty</h3>
        <c:set var="username" value="${param.username}" />
        <c:if test="${empty username}">
            <p><em>No username parameter provided. Try: ?username=John</em></p>
        </c:if>
        <c:if test="${not empty username}">
            <p style="color: blue;">Hello, ${username}!</p>
        </c:if>
    </div>
    
    <!-- Example 10: Generate URL with Parameters -->
    <div class="example">
        <h3>Example 10: Generate URL with Parameters</h3>
        <c:url var="searchUrl" value="/search">
            <c:param name="q" value="JSTL" />
            <c:param name="sort" value="relevance" />
        </c:url>
        <p>Generated URL: <code>${searchUrl}</code></p>
    </div>
    
    <hr>
    <p><small>Try adding parameters to the URL: <code>?username=YourName</code></small></p>
</body>
</html>
