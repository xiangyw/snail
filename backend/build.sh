#!/bin/bash

# Build script for Snail Backend
# This script requires Maven to be installed

echo "Checking for Maven..."
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven to build the project:"
    echo "Ubuntu/Debian: sudo apt install maven"
    echo "CentOS/RHEL: sudo yum install maven or sudo dnf install maven"
    echo "macOS: brew install maven"
    exit 1
fi

echo "Building Snail Backend..."

# Change to project directory
cd "$(dirname "$0")"

# Compile the project
echo "Compiling the project..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
else
    echo "Compilation failed!"
    exit 1
fi

# Run tests
echo "Running tests..."
mvn test

if [ $? -eq 0 ]; then
    echo "All tests passed!"
else
    echo "Some tests failed!"
    exit 1
fi

# Package the application
echo "Packaging the application..."
mvn package -DskipTests

if [ $? -eq 0 ]; then
    echo "Build successful! JAR file created in target/ directory"
    echo "To run the application: java -jar target/snail-backend-0.0.1-SNAPSHOT.jar"
else
    echo "Packaging failed!"
    exit 1
fi