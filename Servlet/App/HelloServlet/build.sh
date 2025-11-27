#!/usr/bin/env bash
set -e

# Simple build script for HelloServlet
# Usage: ./build.sh

# Find CATALINA_HOME if not set
if [ -z "$CATALINA_HOME" ]; then
  if [ -d "/usr/share/tomcat10" ]; then
    CATALINA_HOME="/usr/share/tomcat10"
  elif [ -d "/opt/tomcat" ]; then
    CATALINA_HOME="/opt/tomcat"
  fi
fi

if [ -z "$CATALINA_HOME" ]; then
  echo "Error: CATALINA_HOME is not set. Set it to your Tomcat 10 installation directory and re-run."
  echo "Example: export CATALINA_HOME=/usr/share/tomcat10"
  exit 1
fi

echo "Using CATALINA_HOME=$CATALINA_HOME"

# find servlet API jar
SERVLET_JAR=$(ls "$CATALINA_HOME"/lib/*servlet*.jar 2>/dev/null | head -n1 || true)
if [ -z "$SERVLET_JAR" ]; then
  echo "Could not find servlet jar in $CATALINA_HOME/lib"
  ls "$CATALINA_HOME"/lib || true
  exit 1
fi

echo "Using servlet jar: $SERVLET_JAR"

# compile
mkdir -p WEB-INF/classes
javac -cp "$SERVLET_JAR" -d WEB-INF/classes src/com/example/HelloServlet.java

# package WAR
rm -f HelloServlet.war
jar -cvf HelloServlet.war WEB-INF

echo "Created HelloServlet.war"

# optionally deploy (commented out by default)
# cp HelloServlet.war "$CATALINA_HOME"/webapps/
# echo "Copied HelloServlet.war to $CATALINA_HOME/webapps/"

echo "Done. To deploy: copy HelloServlet.war to $CATALINA_HOME/webapps/ and start/restart Tomcat."
