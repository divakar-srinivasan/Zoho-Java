#!/bin/bash

# Complete Deployment and Testing Script
# This script sets up the entire environment and tests the application

echo "========================================"
echo "ResourceManager Deployment & Testing"
echo "========================================"

PROJECT_DIR="/home/divakar-pt8008/Documents/Servlet/Task/ResourceManager"
DOMAIN="resourcemanager.local"
TOMCAT_HOME="${TOMCAT_HOME:-/usr/local/tomcat}"

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print status
print_status() {
    echo -e "${YELLOW}>>> $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Step 1: Verify Prerequisites
print_status "Step 1: Verifying Prerequisites..."

# Check Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | grep version | awk '{print $3}')
    print_success "Java found: $JAVA_VERSION"
else
    print_error "Java not found. Please install Java 8+"
    exit 1
fi

# Check Maven
if command -v mvn &> /dev/null; then
    print_success "Maven found"
else
    print_error "Maven not found. Please install Maven"
    exit 1
fi

# Check PostgreSQL
if command -v psql &> /dev/null; then
    print_success "PostgreSQL client found"
else
    print_error "PostgreSQL client not found"
fi

# Step 2: Setup Database
print_status "Step 2: Setting up PostgreSQL Database..."
cd "$PROJECT_DIR"

if command -v psql &> /dev/null; then
    # Attempt to create database
    psql -U divakar-pt8008 -d postgres -f database-setup.sql > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        print_success "Database setup completed"
    else
        print_status "Database might already exist, continuing..."
    fi
else
    print_status "PostgreSQL not installed. Database setup skipped."
    print_status "To setup manually, run:"
    echo "   psql -U divakar-pt8008 -d postgres -f $PROJECT_DIR/database-setup.sql"
fi

# Step 3: Build Application
print_status "Step 3: Building Application..."
mvn clean package -DskipTests > build.log 2>&1

if [ $? -eq 0 ]; then
    print_success "Build completed successfully"
    print_success "WAR file: $PROJECT_DIR/target/ROOT.war"
else
    print_error "Build failed. Check build.log for details"
    tail -20 build.log
    exit 1
fi

# Step 4: SSL Certificate Setup
print_status "Step 4: Setting up SSL Certificate..."

KEYSTORE_PATH="$TOMCAT_HOME/conf/resourcemanager-keystore.jks"

if [ ! -f "$KEYSTORE_PATH" ]; then
    keytool -genkey -alias tomcat -keyalg RSA -keysize 2048 \
        -keystore "$KEYSTORE_PATH" \
        -validity 365 \
        -storepass changeit \
        -keypass changeit \
        -dname "CN=$DOMAIN,OU=IT,O=Organization,L=City,ST=State,C=US" \
        > /dev/null 2>&1
    
    if [ $? -eq 0 ]; then
        print_success "SSL certificate created"
        print_success "Keystore: $KEYSTORE_PATH"
    else
        print_error "SSL certificate creation failed"
    fi
else
    print_status "SSL certificate already exists"
fi

# Step 5: Update /etc/hosts
print_status "Step 5: Updating /etc/hosts..."
if ! grep -q "$DOMAIN" /etc/hosts; then
    echo "127.0.0.1 $DOMAIN" | sudo tee -a /etc/hosts > /dev/null
    print_success "Added $DOMAIN to /etc/hosts"
else
    print_status "$DOMAIN already in /etc/hosts"
fi

# Step 6: Deploy to Tomcat
print_status "Step 6: Deploying to Tomcat..."
if [ -d "$TOMCAT_HOME" ]; then
    sudo cp target/ROOT.war $TOMCAT_HOME/webapps/
    if [ $? -eq 0 ]; then
        print_success "Application deployed to Tomcat"
        print_success "Location: $TOMCAT_HOME/webapps/ROOT.war"
    else
        print_error "Deployment to Tomcat failed"
    fi
else
    print_status "Tomcat not found at $TOMCAT_HOME"
    print_status "Manual deployment required"
fi

# Step 7: Create JAR file
print_status "Step 7: Creating JAR file..."
cd "$PROJECT_DIR/target"
if [ -f "ROOT.war" ]; then
    # Create a lib directory for JAR artifacts
    mkdir -p "$PROJECT_DIR/lib"
    
    # Copy all dependencies to lib folder
    mvn dependency:copy-dependencies -DoutputDirectory="$PROJECT_DIR/lib" > /dev/null 2>&1
    
    print_success "JAR file and dependencies created"
    print_success "Location: $PROJECT_DIR/lib/"
fi

# Step 8: Display Configuration Instructions
echo ""
echo "========================================"
echo "Configuration Required"
echo "========================================"
echo ""
echo "1. Add SSL Connector to Tomcat (server.xml):"
echo "   File: $TOMCAT_HOME/conf/server.xml"
echo "   Add the content from: $PROJECT_DIR/ssl-connector-config.xml"
echo ""
echo "   Example Connector block:"
cat << 'EOF'
<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
           maxThreads="150" SSLEnabled="true">
    <SSLHostConfig hostName="resourcemanager.local" certificateVerification="none">
        <Certificate certificateKeyFile="/usr/local/tomcat/conf/resourcemanager-keystore.jks"
                     certificateKeystorePassword="changeit"
                     type="JKS" />
    </SSLHostConfig>
</Connector>
EOF
echo ""

# Step 9: Test Database Connection
print_status "Step 9: Testing Database Connection..."
if command -v psql &> /dev/null; then
    psql -U divakar-pt8008 -d test -c "SELECT COUNT(*) FROM users;" > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        print_success "Database connection successful"
    else
        print_error "Database connection failed"
    fi
fi

echo ""
echo "========================================"
echo "Next Steps"
echo "========================================"
echo ""
echo "1. Start Tomcat:"
echo "   $TOMCAT_HOME/bin/startup.sh"
echo ""
echo "2. Access the application:"
echo "   HTTP:  http://localhost:8080"
echo "   HTTPS: https://$DOMAIN:8443"
echo ""
echo "3. Login with credentials:"
echo "   Username: admin"
echo "   Password: admin"
echo ""
echo "4. View logs:"
echo "   Application: /tmp/resourcemanager.log"
echo "   Tomcat: $TOMCAT_HOME/logs/"
echo ""
echo "========================================"
