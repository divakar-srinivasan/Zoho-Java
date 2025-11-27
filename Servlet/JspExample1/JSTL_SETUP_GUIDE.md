# JSTL Setup - Quick Reference Guide

## What is JSTL?
JSTL (JavaServer Pages Standard Tag Library) provides a set of custom JSP tags for common tasks like:
- Variable management
- Conditional logic
- Iteration/loops
- String manipulation
- Date/Number formatting
- URL handling

## Setup Completed ✓

### 1. Directory Structure Created
```
JspExample1/
├── WEB-INF/
│   ├── web.xml                          (Deployment descriptor)
│   └── lib/
│       ├── jakarta.servlet.jsp.jstl-api-3.0.0.jar
│       └── jakarta.servlet.jsp.jstl-3.0.1.jar
├── index.jsp                            (Original file)
└── jstl-example.jsp                     (JSTL comprehensive example)
```

### 2. Required JAR Files Downloaded
- **jakarta.servlet.jsp.jstl-api-3.0.0.jar** - JSTL API (45 KB)
- **jakarta.servlet.jsp.jstl-3.0.1.jar** - JSTL Implementation (3.5 MB)

### 3. Key JSTL Taglib Directives

#### Core Tags (Variable, Conditional, Loop)
```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
```

#### Formatting Tags (Date, Number, Locale)
```jsp
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
```

#### Function Tags (String manipulation)
```jsp
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
```

## Common JSTL Tags

### Core Tags (c:)
| Tag | Purpose |
|-----|---------|
| `<c:set>` | Set variable in scope |
| `<c:out>` | Output variable value |
| `<c:if>` | Conditional execution |
| `<c:choose>` | Multiple conditions (if-else) |
| `<c:when>` | Condition option in choose |
| `<c:otherwise>` | Default case in choose |
| `<c:forEach>` | Loop/iterate |
| `<c:forTokens>` | Loop through tokens |
| `<c:url>` | Create URL with parameters |
| `<c:redirect>` | Redirect to URL |
| `<c:catch>` | Error handling |

### Formatting Tags (fmt:)
| Tag | Purpose |
|-----|---------|
| `<fmt:formatDate>` | Format date/time |
| `<fmt:formatNumber>` | Format numbers |
| `<fmt:parseDate>` | Parse date string |
| `<fmt:parseNumber>` | Parse number string |
| `<fmt:setLocale>` | Set locale |
| `<fmt:timeZone>` | Set timezone |

### Function Tags (fn:)
| Function | Purpose |
|----------|---------|
| `fn:length()` | String/collection length |
| `fn:toUpperCase()` | Convert to uppercase |
| `fn:toLowerCase()` | Convert to lowercase |
| `fn:substring()` | Extract substring |
| `fn:replace()` | Replace text |
| `fn:split()` | Split string into array |
| `fn:join()` | Join array into string |
| `fn:contains()` | Check if contains |
| `fn:startsWith()` | Check if starts with |
| `fn:endsWith()` | Check if ends with |
| `fn:indexOf()` | Find index of string |
| `fn:trim()` | Remove whitespace |

## How to Access the Examples

1. **Start Tomcat:**
   ```bash
   cd /home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/bin
   ./startup.sh
   ```

2. **Access in Browser:**
   - http://localhost:8080/JspExample1/jstl-example.jsp
   - http://localhost:8080/JspExample1/index.jsp

3. **Test URL Parameters:**
   - http://localhost:8080/JspExample1/jstl-example.jsp?username=John&email=john@example.com

## Example Usage

### Simple Variable Output
```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="greeting" value="Hello JSTL!" />
<p><c:out value="${greeting}" /></p>
```

### Conditional Logic
```jsp
<c:if test="${age >= 18}">
    <p>You are an adult.</p>
</c:if>
```

### Loop Through Collection
```jsp
<c:forEach var="item" items="${itemList}">
    <p>${item}</p>
</c:forEach>
```

### Format Date
```jsp
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:formatDate value="${date}" pattern="dd/MM/yyyy HH:mm:ss" />
```

### String Functions
```jsp
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
${fn:toUpperCase("hello")} <!-- HELLO -->
${fn:length("JSTL")} <!-- 4 -->
```

## Notes
- Using Jakarta EE namespace (jakarta.tags.*) for Tomcat 10+
- For Tomcat 9 and earlier, use JSP Standard Tag Library (javax.servlet.jsp.jstl.* prefix)
- All required JSTL libraries are already in WEB-INF/lib/
- No additional configuration needed in web.xml for JSTL
