<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>JSTL Example Application</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h2 { color: #333; border-bottom: 2px solid #007bff; padding-bottom: 10px; }
        .section { margin: 20px 0; padding: 15px; background-color: #f5f5f5; border-radius: 5px; }
        table { border-collapse: collapse; width: 100%; margin-top: 10px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #007bff; color: white; }
    </style>
</head>
<body>
    <h1>JSTL (JavaServer Pages Standard Tag Library) Examples</h1>
    
    <!-- 1. Core Tags - Variable Initialization and Output -->
    <div class="section">
        <h2>1. Core Tags - Variable and Output</h2>
        <c:set var="name" value="John Doe" />
        <c:set var="age" value="28" />
        <p>Name: <c:out value="${name}" default="Unknown" /></p>
        <p>Age: <c:out value="${age}" /> years old</p>
    </div>
    
    <!-- 2. Conditional Tags -->
    <div class="section">
        <h2>2. Conditional Tags - if/choose</h2>
        <c:set var="score" value="85" />
        
        <p><strong>Using c:if:</strong></p>
        <c:if test="${score >= 80}">
            <p>✓ Excellent score! (${score})</p>
        </c:if>
        
        <p><strong>Using c:choose:</strong></p>
        <c:choose>
            <c:when test="${score >= 90}">
                <p>Grade: A (90-100)</p>
            </c:when>
            <c:when test="${score >= 80}">
                <p>Grade: B (80-89)</p>
            </c:when>
            <c:when test="${score >= 70}">
                <p>Grade: C (70-79)</p>
            </c:when>
            <c:otherwise>
                <p>Grade: F (Below 70)</p>
            </c:otherwise>
        </c:choose>
    </div>
    
    <!-- 3. Loop Tags -->
    <div class="section">
        <h2>3. Loop Tags - forEach</h2>
        
        <p><strong>Looping through a list:</strong></p>
        <c:set var="fruits">
            <c:forEach var="fruit" items="Apple,Banana,Orange,Mango,Strawberry" varStatus="loop">
                <c:if test="${loop.index == 1}">Apple</c:if>
                <c:if test="${loop.index == 2}">Banana</c:if>
                <c:if test="${loop.index == 3}">Orange</c:if>
                <c:if test="${loop.index == 4}">Mango</c:if>
                <c:if test="${loop.index == 5}">Strawberry</c:if>
            </c:forEach>
        </c:set>
        
        <table>
            <tr>
                <th>Index</th>
                <th>Fruit</th>
                <th>Count</th>
                <th>Is First?</th>
                <th>Is Last?</th>
            </tr>
            <c:forEach var="fruit" items="Apple,Banana,Orange,Mango,Strawberry" varStatus="status">
                <tr>
                    <td>${status.index}</td>
                    <td>${fruit}</td>
                    <td>${status.count}</td>
                    <td>
                        <c:if test="${status.first}">Yes</c:if>
                        <c:if test="${!status.first}">No</c:if>
                    </td>
                    <td>
                        <c:if test="${status.last}">Yes</c:if>
                        <c:if test="${!status.last}">No</c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    
    <!-- 4. String Functions -->
    <div class="section">
        <h2>4. String Functions (fn: prefix)</h2>
        <c:set var="message" value="Welcome to JSTL Examples" />
        <p>Original: ${message}</p>
        <p>Length: ${fn:length(message)} characters</p>
        <p>Uppercase: ${fn:toUpperCase(message)}</p>
        <p>Lowercase: ${fn:toLowerCase(message)}</p>
        <p>Contains "JSTL": ${fn:contains(message, 'JSTL')}</p>
        <p>Starts with "Welcome": ${fn:startsWith(message, 'Welcome')}</p>
        <p>Substring (0,7): ${fn:substring(message, 0, 7)}</p>
        <p>Replace "JSTL" with "JSP": ${fn:replace(message, 'JSTL', 'JSP')}</p>
    </div>
    
    <!-- 5. Formatting Tags -->
    <div class="section">
        <h2>5. Formatting Tags - Date and Number</h2>
        <c:set var="now" value="<%= new java.util.Date() %>" />
        <c:set var="price" value="1234.5678" />
        
        <p><strong>Date Formatting:</strong></p>
        <p>Current Date: <fmt:formatDate value="${now}" type="date" dateStyle="long" /></p>
        <p>Current Time: <fmt:formatDate value="${now}" type="time" timeStyle="long" /></p>
        <p>Full DateTime: <fmt:formatDate value="${now}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
        
        <p><strong>Number Formatting:</strong></p>
        <p>Original Price: ${price}</p>
        <p>Formatted Price: <fmt:formatNumber value="${price}" type="currency" currencySymbol="$" /></p>
        <p>Percentage: <fmt:formatNumber value="0.85" type="percent" /></p>
        <p>With 2 Decimals: <fmt:formatNumber value="${price}" maxFractionDigits="2" /></p>
    </div>
    
    <!-- 6. URL and Form Parameters -->
    <div class="section">
        <h2>6. URL Handling</h2>
        <c:set var="baseUrl" value="http://example.com/search" />
        <c:url var="searchUrl" value="/search">
            <c:param name="q" value="JSTL tutorial" />
            <c:param name="lang" value="en" />
        </c:url>
        <p>Generated URL: ${searchUrl}</p>
    </div>
    
    <!-- 7. Error Handling with catch -->
    <div class="section">
        <h2>7. Error Handling - c:catch</h2>
        <c:catch var="error">
            <c:set var="result" value="${10 / 2}" />
            <p>10 / 2 = ${result}</p>
        </c:catch>
        <c:if test="${error != null}">
            <p>Error occurred: ${error.message}</p>
        </c:if>
    </div>
    
    <!-- 8. Request Parameters -->
    <div class="section">
        <h2>8. Request Parameters</h2>
        <c:set var="username" value="${param.username}" />
        <c:set var="email" value="${param.email}" />
        
        <p>Username Parameter: 
            <c:if test="${empty username}">
                <em>Not provided</em>
            </c:if>
            <c:if test="${not empty username}">
                ${username}
            </c:if>
        </p>
        <p>Email Parameter: 
            <c:if test="${empty email}">
                <em>Not provided</em>
            </c:if>
            <c:if test="${not empty email}">
                ${email}
            </c:if>
        </p>
    </div>
    
    <!-- 9. Summary Section -->
    <div class="section">
        <h2>JSTL Tags Summary</h2>
        <table>
            <tr>
                <th>Category</th>
                <th>Prefix</th>
                <th>URI</th>
                <th>Description</th>
            </tr>
            <tr>
                <td>Core</td>
                <td>c:</td>
                <td>jakarta.tags.core</td>
                <td>Variables, conditionals, loops, URL redirection</td>
            </tr>
            <tr>
                <td>Formatting</td>
                <td>fmt:</td>
                <td>jakarta.tags.fmt</td>
                <td>Date, time, number formatting, localization</td>
            </tr>
            <tr>
                <td>Functions</td>
                <td>fn:</td>
                <td>jakarta.tags.functions</td>
                <td>String manipulation functions</td>
            </tr>
        </table>
    </div>
    
    <hr>
    <footer>
        <p><small>JSTL Example Application | Generated on <fmt:formatDate value="${now}" pattern="dd/MM/yyyy HH:mm:ss" /></small></p>
    </footer>
</body>
</html>
