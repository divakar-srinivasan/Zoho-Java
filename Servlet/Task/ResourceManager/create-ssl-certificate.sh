#!/bin/bash

# This script creates a self-signed SSL certificate for Tomcat
# Run this script to generate the keystore with your custom domain

DOMAIN="${1:-resourcemanager.local}"
KEYSTORE_PATH="${2:-/usr/local/tomcat/conf/resourcemanager-keystore.jks}"
KEYSTORE_PASSWORD="${3:-changeit}"

echo "Creating self-signed certificate for domain: $DOMAIN"
echo "Keystore path: $KEYSTORE_PATH"

# Create the certificate
keytool -genkey -alias tomcat -keyalg RSA -keysize 2048 \
    -keystore "$KEYSTORE_PATH" \
    -validity 365 \
    -storepass "$KEYSTORE_PASSWORD" \
    -keypass "$KEYSTORE_PASSWORD" \
    -dname "CN=$DOMAIN,OU=IT,O=Organization,L=City,ST=State,C=US"

if [ $? -eq 0 ]; then
    echo "Certificate created successfully!"
    echo "Keystore file: $KEYSTORE_PATH"
    echo "Store password: $KEYSTORE_PASSWORD"
    echo "Key password: $KEYSTORE_PASSWORD"
    echo ""
    echo "Next steps:"
    echo "1. Add the following to your /etc/hosts file:"
    echo "   127.0.0.1 $DOMAIN"
    echo "2. Update Tomcat's server.xml with SSL connector configuration"
    echo "3. Access the application at: https://$DOMAIN:8443"
else
    echo "Certificate creation failed!"
    exit 1
fi
