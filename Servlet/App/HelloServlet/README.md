Simple HelloServlet (Tomcat 10 / Jakarta Servlet API)

Overview
- This small example demonstrates a servlet using the Jakarta Servlet API (Tomcat 10+).
- The servlet responds to GET /hello and returns a basic HTML page.

Files created
- `src/com/example/HelloServlet.java` — servlet source (package com.example)
- `WEB-INF/web.xml` — servlet mapping and descriptor
- `build.sh` — small helper script to compile and create a WAR

Prerequisites
- Java is already installed (you told me it is).
- Tomcat 10 is already installed. The script expects $CATALINA_HOME to be set, or checks common locations (/usr/share/tomcat10, /opt/tomcat).

Steps to compile, build and deploy

1) Open a terminal and cd to the project root:

   cd /home/divakar-pt8008/Documents/Servlet/App/HelloServlet

2) Make the build script executable (only first time):

   chmod +x build.sh

3) (Optional) If you don't have CATALINA_HOME set, set it now. Example:

   export CATALINA_HOME=/usr/share/tomcat10

   If you installed Tomcat elsewhere, point CATALINA_HOME there.

4) Compile and create the WAR:

   ./build.sh

   If compilation fails because the servlet JAR can't be found, run this to inspect Tomcat's lib folder:

   ls "$CATALINA_HOME"/lib | grep -i servlet

5) Deploy the WAR to Tomcat:

   sudo cp HelloServlet.war "$CATALINA_HOME"/webapps/

   (If Tomcat is running, it will auto-deploy the WAR. Otherwise start Tomcat.)

   To start/stop Tomcat you can use systemctl (if managed by systemd), e.g.:

   sudo systemctl restart tomcat10

   or use Tomcat's scripts:

   "$CATALINA_HOME"/bin/startup.sh
   "$CATALINA_HOME"/bin/shutdown.sh

6) Test in the browser

   Open: http://localhost:8080/HelloServlet/hello

   - `HelloServlet` is the WAR name (directory created by Tomcat). If you rename the WAR, use that name.
   - `8080` is the default Tomcat HTTP port; change if your Tomcat uses a different port.

Troubleshooting
- 404 after deploy: Check Tomcat logs in `$CATALINA_HOME/logs/` (catalina.out, localhost.*.log).
- Compilation error about jakarta.servlet package: Ensure you're running with the servlet JAR from Tomcat 10. The project uses Jakarta package names (jakarta.servlet.*).
- Permission denied when copying to webapps: use sudo or run as the Tomcat owner.

That's it — a minimal HelloServlet for Tomcat 10. If you want, I can compile and deploy it now on your machine (I'll need permission to access Tomcat's directories).