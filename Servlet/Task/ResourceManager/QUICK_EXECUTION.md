# RESOURCEMANAGER - QUICK EXECUTION GUIDE

## 🚀 FASTEST WAY TO RUN (ONE COMMAND)

```bash
cd /home/divakar-pt8008/Documents/Servlet/Task/ResourceManager
./complete-deployment.sh
```

Then open: **http://localhost:8080**

Login: `admin` / `admin`

---

## 📋 STEP-BY-STEP EXECUTION

### Prerequisites
- [ ] Java 1.8+ installed: `java -version`
- [ ] Maven installed: `mvn -version`
- [ ] PostgreSQL running: `sudo systemctl start postgresql`
- [ ] Tomcat installed: `$TOMCAT_HOME` set
- [ ] Internet connection (first build downloads dependencies)

### Step 1: Build
```bash
cd /home/divakar-pt8008/Documents/Servlet/Task/ResourceManager
mvn clean package -DskipTests
```
✓ Creates: `target/ROOT.war` (3.6 MB)

### Step 2: Setup Database
```bash
psql -U divakar-pt8008 -d postgres -f database-setup.sql
```
✓ Creates tables: `users`, `resources`
✓ Loads sample data

### Step 3: Generate SSL Certificate
```bash
./create-ssl-certificate.sh resourcemanager.local
```

### Step 4: Add Domain to Hosts
```bash
echo "127.0.0.1 resourcemanager.local" | sudo tee -a /etc/hosts
```

### Step 5: Deploy to Tomcat
```bash
cp target/ROOT.war $TOMCAT_HOME/webapps/
```

### Step 6: Start Tomcat
```bash
$TOMCAT_HOME/bin/startup.sh
```

Check if running:
```bash
tail -20 $TOMCAT_HOME/logs/catalina.out
```

### Step 7: Access Application
- **HTTP**: http://localhost:8080
- **HTTPS**: https://resourcemanager.local:8443

---

## ✅ LOGIN CREDENTIALS

| Field | Value |
|-------|-------|
| Username | admin |
| Password | admin |
| Session Timeout | 5 minutes |

---

## 🧪 TEST THE APPLICATION

### Automated Test
```bash
./test-application.sh
```

### Manual Tests

**1. Test Login:**
```bash
curl -X POST http://localhost:8080/login \
  -d "username=admin&password=admin" \
  -c /tmp/cookies.txt -L
```

**2. Test API:**
```bash
curl http://localhost:8080/api/v1/resourcemanager/resource/1 \
  -b /tmp/cookies.txt
```

Expected: `{"id":"res1","name":"Database Server"}`

**3. View Application Logs:**
```bash
tail -f /tmp/resourcemanager.log
```

---

## 🔧 COMMON TASKS

| Task | Command |
|------|---------|
| Stop Tomcat | `$TOMCAT_HOME/bin/shutdown.sh` |
| Check if running | `ps aux \| grep tomcat` |
| Rebuild only | `./build.sh` |
| View Tomcat logs | `tail -f $TOMCAT_HOME/logs/catalina.out` |
| View app logs | `tail -f /tmp/resourcemanager.log` |
| Check ports | `lsof -i :8080` |
| Reset database | `psql -U divakar-pt8008 -d postgres -f database-setup.sql` |

---

## 📁 PROJECT STRUCTURE

```
ResourceManager/
├── src/main/java/com/resourcemanager/
│   ├── servlet/          (LoginServlet, ResourceServlet, etc.)
│   ├── filter/           (AuthenticationFilter)
│   ├── dao/              (UserDAO, ResourceDAO)
│   └── util/             (DatabaseConnection, Logger)
├── src/main/webapp/
│   ├── login.jsp         (Login page)
│   ├── home.jsp          (Dashboard)
│   ├── index.jsp         (Redirect logic)
│   └── WEB-INF/web.xml   (Configuration)
├── target/ROOT.war       (Built application)
├── pom.xml               (Maven config)
├── database-setup.sql    (Database schema)
├── *.sh                  (Deployment scripts)
└── *.md                  (Documentation)
```

---

## ⚙️ CONFIGURATION

| Item | Value |
|------|-------|
| Application Port (HTTP) | 8080 |
| Application Port (HTTPS) | 8443 |
| Database | PostgreSQL on localhost:5432 |
| Database Name | test |
| Database User | divakar-pt8008 |
| Domain | resourcemanager.local |
| SSL Keystore | $TOMCAT_HOME/conf/resourcemanager-keystore.jks |
| SSL Password | changeit |
| Session Timeout | 5 minutes |
| Log File | /tmp/resourcemanager.log |

---

## 🐛 TROUBLESHOOTING

| Problem | Solution |
|---------|----------|
| Build fails | Check Java & Maven: `java -version && mvn -version` |
| DB connection fails | Check PostgreSQL: `sudo systemctl status postgresql` |
| Port 8080 in use | Kill process: `lsof -i :8080 && kill -9 <PID>` |
| Certificate error | Regenerate: `./create-ssl-certificate.sh resourcemanager.local` |
| Cannot access domain | Add to hosts: `echo "127.0.0.1 resourcemanager.local" \| sudo tee -a /etc/hosts` |
| Tomcat won't start | Check logs: `tail -50 $TOMCAT_HOME/logs/catalina.out` |

---

## 📊 FEATURES IMPLEMENTED

✅ Login/Logout with session management
✅ Home page dashboard with sidebar
✅ REST API: `/api/v1/resourcemanager/{resource}/{id}`
✅ Authentication filter on all requests
✅ 5-minute session timeout with automatic redirect
✅ AJAX resource loading
✅ File-based logging (/tmp/resourcemanager.log)
✅ PostgreSQL database integration
✅ SSL/TLS HTTPS support
✅ Self-signed certificate
✅ Custom domain mapping

---

## 📚 DOCUMENTATION

- `README.md` - Complete project guide
- `INSTALLATION_GUIDE.txt` - Detailed installation
- `DEPLOYMENT_GUIDE.md` - Deployment instructions
- `PROJECT_SUMMARY.md` - Technical overview
- `index.html` - Interactive summary (open in browser)

---

**Ready to execute? Start with:**
```bash
cd /home/divakar-pt8008/Documents/Servlet/Task/ResourceManager
./complete-deployment.sh
```
