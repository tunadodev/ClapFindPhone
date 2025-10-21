#!/bin/bash

# Set JAVA_HOME to JDK 17
export JAVA_HOME=/Users/tunado/Library/Java/JavaVirtualMachines/jbr-17.0.8/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

# Verify Java version
echo "Using Java version:"
java -version

# Clean and build
echo "Cleaning project..."
./gradlew clean

echo "Building project..."
./gradlew app:assembleVOfficial_Debug

echo "Build completed!"


