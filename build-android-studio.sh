#!/bin/bash

# Script to build project with correct JDK for Android Studio
# Usage: ./build-android-studio.sh

echo "🔧 Setting up environment for Android Studio build..."

# Set JAVA_HOME to JDK 17
export JAVA_HOME=/Users/tunado/Library/Java/JavaVirtualMachines/jbr-17.0.8/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

# Set Gradle JVM args
export GRADLE_OPTS="-Xmx4g -Dfile.encoding=UTF-8 -XX:+UseParallelGC"

# Verify Java version
echo "📋 Using Java version:"
java -version

echo ""
echo "🧹 Cleaning project..."
./gradlew clean

echo ""
echo "🔨 Building project..."
./gradlew app:assembleVOfficial_Debug

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build completed successfully!"
    echo "📦 APK location:"
    find app/build/outputs -name "*.apk" -type f
else
    echo ""
    echo "❌ Build failed!"
    exit 1
fi

