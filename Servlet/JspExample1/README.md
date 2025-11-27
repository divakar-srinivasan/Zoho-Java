# JSTL Setup Complete ✓

## What Was Done

### 1. **Downloaded JSTL Libraries** 📦
   - `jakarta.servlet.jsp.jstl-api-3.0.0.jar` (45 KB)
   - `jakarta.servlet.jsp.jstl-3.0.1.jar` (3.5 MB)
   - Location: `WEB-INF/lib/`

### 2. **Created Directory Structure** 📁
   ```
   JspExample1/
   ├── WEB-INF/
   │   ├── web.xml
   │   └── lib/
   │       ├── jakarta.servlet.jsp.jstl-api-3.0.0.jar
   │       └── jakarta.servlet.jsp.jstl-3.0.1.jar
   ├── index.jsp (original)
   ├── jstl-example.jsp (comprehensive examples)
   ├── jstl-quick-start.jsp (simple starter)
   └── README.md (this file)
   ```

### 3. **Created Example Files**

#### **jstl-quick-start.jsp** - Start here! ⭐
- 10 simple, practical examples
- Variables, conditionals, loops
- Date/number formatting
- String functions
- Perfect for beginners

#### **jstl-example.jsp** - Comprehensive tutorial
- All JSTL features demonstrated
- Variables and output
- Conditional logic (if/choose)
- Loops with varStatus
- String functions
- Date/number formatting
- URL handling
- Error handling

## How to Use

### Start Tomcat
```bash
cd /home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/bin
./startup.sh
```

### Access the Pages in Browser
1. **Quick Start (Recommended):** 
   - http://localhost:8080/JspExample1/jstl-quick-start.jsp

2. **Comprehensive Examples:**
   - http://localhost:8080/JspExample1/jstl-example.jsp

3. **Original Page:**
   - http://localhost:8080/JspExample1/index.jsp

### Test with URL Parameters
```
http://localhost:8080/JspExample1/jstl-quick-start.jsp?username=YourName
```

## Quick Reference

### Import JSTL Libraries (Add to your JSP)
```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
```

### Most Used Tags

**Set Variable:**
```jsp
<c:set var="name" value="value" />
```

**Output Variable:**
```jsp
<c:out value="${name}" />
```

**Conditional:**
```jsp
<c:if test="${condition}">
    <!-- content -->
</c:if>
```

**Loop:**
```jsp
<c:forEach var="item" items="list">
    ${item}
</c:forEach>
```

**Format Date:**
```jsp
<fmt:formatDate value="${date}" pattern="dd/MM/yyyy" />
```

**String Functions:**
```jsp
${fn:toUpperCase(text)}
${fn:length(text)}
${fn:contains(text, 'keyword')}
```

## Common Troubleshooting

### Issue: Tags not recognized
- ✓ **Solution:** Ensure JAR files are in `WEB-INF/lib/`
- ✓ **Solution:** Check taglib URI is correct (`jakarta.tags.*` for Tomcat 10+)

### Issue: Page shows raw code
- ✓ **Solution:** Restart Tomcat
- ✓ **Solution:** Clear browser cache (Ctrl+F5)

### Issue: Compilation error
- ✓ **Solution:** Check JSP syntax (proper quotes, brackets)
- ✓ **Solution:** Verify all variables are defined before use

## Key Features Demonstrated

✓ Variables and scoping
✓ Conditional logic (if/choose/when)
✓ Loops and iteration
✓ String manipulation
✓ Date/time formatting
✓ Number formatting
✓ URL generation with parameters
✓ Request parameter handling
✓ Empty/null checking
✓ Error handling

## Next Steps

1. ✓ Review `jstl-quick-start.jsp` to understand basics
2. ✓ Explore `jstl-example.jsp` for advanced features
3. ✓ Create your own JSP pages using JSTL
4. ✓ Read `JSTL_SETUP_GUIDE.md` for comprehensive documentation

## Versions Used

- **Jakarta EE:** 10.0+ (Tomcat 10.1+)
- **JSTL API:** 3.0.0
- **JSTL Implementation:** 3.0.1
- **Namespace:** jakarta.tags.* (not javax.servlet.jsp.jstl)

---

**Happy coding with JSTL!** 🚀
