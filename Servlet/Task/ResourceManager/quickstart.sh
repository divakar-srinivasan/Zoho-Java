#!/bin/bash

# Quick Start Guide - Run this script to start the application

echo "========================================"
echo "ResourceManager Quick Start"
echo "========================================"

PROJECT_DIR="/home/divakar-pt8008/Documents/Servlet/Task/ResourceManager"
TOMCAT_HOME="${TOMCAT_HOME:-/usr/local/tomcat}"

# Function to print colored output
print_info() {
    echo -e "\033[0;33m>>> $1\033[0m"
}

print_success() {
    echo -e "\033[0;32m✓ $1\033[0m"
}

print_error() {
    echo -e "\033[0;31m✗ $1\033[0m"
}

# Check if Tomcat is running
print_info "Checking Tomcat status..."
if pgrep -f "tomcat" > /dev/null; then
    print_success "Tomcat is already running"
    
    echo ""
    echo "Application URLs:"
    echo "  HTTP:  http://localhost:8080"
    echo "  HTTPS: https://resourcemanager.local:8443"
    echo ""
    echo "Login credentials:"
    echo "  Username: admin"
    echo "  Password: admin"
    echo ""
else
    print_info "Starting Tomcat..."
    
    if [ -f "$TOMCAT_HOME/bin/startup.sh" ]; then
        $TOMCAT_HOME/bin/startup.sh
        
        print_success "Tomcat started"
        print_info "Waiting for application to deploy..."
        sleep 5
        
        echo ""
        echo "Application is starting at:"
        echo "  HTTP:  http://localhost:8080"
        echo "  HTTPS: https://resourcemanager.local:8443"
        echo ""
        echo "Login with:"
        echo "  Username: admin"
        echo "  Password: admin"
        echo ""
        print_info "Check $TOMCAT_HOME/logs/catalina.out for startup logs"
    else
        print_error "Tomcat startup script not found at: $TOMCAT_HOME/bin/startup.sh"
        echo ""
        echo "Please set TOMCAT_HOME environment variable or install Tomcat"
    fi
fi

echo ""
echo "========================================"
echo "Useful Commands:"
echo "========================================"
echo ""
echo "View application logs:"
echo "  tail -f /tmp/resourcemanager.log"
echo ""
echo "View Tomcat logs:"
echo "  tail -f $TOMCAT_HOME/logs/catalina.out"
echo ""
echo "Stop Tomcat:"
echo "  $TOMCAT_HOME/bin/shutdown.sh"
echo ""
echo "Check application status:"
echo "  curl http://localhost:8080"
echo ""
