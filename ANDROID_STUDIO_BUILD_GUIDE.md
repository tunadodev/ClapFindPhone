# 🔧 Hướng dẫn Build với Android Studio

## ❌ Vấn đề đã khắc phục
1. **JdkImageTransform error**: Android Studio sử dụng JDK embedded thay vì JDK 17
2. **Room Database error**: `RoomDatabase_Impl does not exist` do KAPT không tương thích với JDK 17

## ✅ Các giải pháp đã áp dụng

### 1. **Sử dụng Script Build (Khuyến nghị)**
```bash
./build-android-studio.sh
```
Script này sẽ:
- Set JAVA_HOME đúng JDK 17
- Clean và build project
- Hiển thị kết quả build

### 2. **Cấu hình Android Studio thủ công**

#### **Bước 1: Cấu hình Gradle JDK**
1. Mở **Android Studio**
2. **File → Settings** (hoặc **Android Studio → Preferences** trên macOS)
3. **Build, Execution, Deployment → Build Tools → Gradle**
4. **Gradle JDK**: Chọn **JDK 17** thay vì "Use Embedded JDK"
   - Path: `/Users/tunado/Library/Java/JavaVirtualMachines/jbr-17.0.8/Contents/Home`

#### **Bước 2: Cấu hình Project Structure**
1. **File → Project Structure**
2. **SDK Location**
3. **JDK Location**: Set về JDK 17

#### **Bước 3: Invalidate Caches**
1. **File → Invalidate Caches and Restart**
2. Chọn **Invalidate and Restart**

### 3. **Files đã được cấu hình**

#### **app/build.gradle**
```gradle
plugins {
    id 'com.google.devtools.ksp' version '1.8.22-1.0.11'
}

dependencies {
    // Room with KSP (thay vì KAPT)
    implementation 'androidx.room:room-ktx:2.5.2'
    ksp "androidx.room:room-compiler:2.5.2"
    
    // Lifecycle with annotationProcessor
    annotationProcessor "android.arch.lifecycle:compiler:1.1.1"
}
```

#### **gradle.properties**
```properties
org.gradle.java.home=/Users/tunado/Library/Java/JavaVirtualMachines/jbr-17.0.8/Contents/Home
```

#### **local.properties**
```properties
sdk.dir=/Users/tunado/Library/Android/sdk
org.gradle.java.home=/Users/tunado/Library/Java/JavaVirtualMachines/jbr-17.0.8/Contents/Home
gradle.java.home=/Users/tunado/Library/Java/JavaVirtualMachines/jbr-17.0.8/Contents/Home
```

#### **~/.gradle/gradle.properties** (Global)
```properties
org.gradle.java.home=/Users/tunado/Library/Java/JavaVirtualMachines/jbr-17.0.8/Contents/Home
```

## 🚀 Kết quả
- ✅ Terminal build: `./gradlew app:assembleVOfficial_Debug` - **SUCCESS**
- ✅ Script build: `./build-android-studio.sh` - **SUCCESS**
- ✅ Android Studio build: Sau khi cấu hình - **SUCCESS**

## 📝 Lưu ý
- **KSP thay thế KAPT** cho Room Database (tương thích tốt hơn với JDK 17)
- **annotationProcessor** cho Lifecycle components
- **Namespace đã được thêm** vào tất cả modules
- **JDK 17** được sử dụng thống nhất

## 🔍 Troubleshooting
Nếu vẫn gặp lỗi:
1. Restart Android Studio
2. Clean project: **Build → Clean Project**
3. Rebuild project: **Build → Rebuild Project**
4. Sử dụng script build: `./build-android-studio.sh`
