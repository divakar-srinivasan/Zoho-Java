#!/bin/bash

# Build the project
echo "Building ResourceManager application..."
cd /home/divakar-pt8008/Documents/Servlet/Task/ResourceManager

mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "WAR file location: target/ROOT.war"
    echo "JAR file will be created with dependencies"
else
    echo "Build failed!"
    exit 1
fi
