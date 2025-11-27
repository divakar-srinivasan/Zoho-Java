#!/bin/bash

# Tomcat SSL Deployment Script
# This script sets up SSL for Tomcat and deploys the application

TOMCAT_HOME="/usr/local/tomcat"
DOMAIN="resourcemanager.local"
KEYSTORE_PASSWORD="changeit"

echo "=========================================="
echo "Tomcat SSL Setup and Deployment"
echo "=========================================="

# Step 1: Create SSL certificate
echo ""
echo "Step 1: Creating SSL Certificate..."
"$(dirname "$0")/create-ssl-certificate.sh" "$DOMAIN" "$TOMCAT_HOME/conf/resourcemanager-keystore.jks" "$KEYSTORE_PASSWORD"

# Step 2: Update hosts file
echo ""
echo "Step 2: Updating /etc/hosts file..."
if ! grep -q "$DOMAIN" /etc/hosts; then
    echo "127.0.0.1 $DOMAIN" | sudo tee -a /etc/hosts
    echo "Added $DOMAIN to /etc/hosts"
else
    echo "$DOMAIN already exists in /etc/hosts"
fi

# Step 3: Copy WAR file to Tomcat
echo ""
echo "Step 3: Deploying application..."
if [ -f "target/ROOT.war" ]; then
    sudo cp target/ROOT.war $TOMCAT_HOME/webapps/
    echo "Application deployed to $TOMCAT_HOME/webapps/ROOT.war"
else
    echo "ERROR: target/ROOT.war not found. Please build the project first."
    exit 1
fi

# Step 4: Instructions
echo ""
echo "=========================================="
echo "Setup Instructions:"
echo "=========================================="
echo ""
echo "1. Update Tomcat's server.xml:"
echo "   Location: $TOMCAT_HOME/conf/server.xml"
echo "   Add the SSL connector from: ssl-connector-config.xml"
echo ""
echo "2. Start/Restart Tomcat:"
echo "   $TOMCAT_HOME/bin/startup.sh"
echo ""
echo "3. Access the application:"
echo "   https://$DOMAIN:8443"
echo ""
echo "4. View Tomcat logs:"
echo "   $TOMCAT_HOME/logs/catalina.out"
echo ""
echo "=========================================="
