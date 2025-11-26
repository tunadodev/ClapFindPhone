import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.android.application") version "8.4.0"
    id("org.jetbrains.kotlin.android") version "2.1.0"
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution") version "5.0.0"
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
}

android {
    namespace = "com.ibl.tool.clapfindphone"
    compileSdk = 35

    defaultConfig {
        applicationId = "claptofindphone.antitheft.antitouch"
        minSdk = 23
        targetSdk = 35
        versionCode = 10050
        versionName = "1.0.5"

        val formattedDate = SimpleDateFormat("MMM.dd.yyyy.hh.mm.ss", Locale.getDefault()).format(Date())
        setProperty("archivesBaseName", "${namespace}_V${versionName}_${formattedDate}")

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    signingConfigs {
        create("release") {
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")


        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    flavorDimensions += "adIds"
    productFlavors {
        create("appDev") {
            // Use test ID during development
            manifestPlaceholders["ad_app_id"] = "ca-app-pub-3940256099942544~3347511713"
            buildConfigField("String", "key_max", "\"pmaJXAUhzJmbd_UelyvIXNuTfnk5SrmGRbENaSg47iJv8ETOKgTGVbrihsAOVaIF8csgk6LbGUf51PA5HuMpBO\"")

            buildConfigField("String", "_101_spl_inter", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "_102_spl_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_103_spl_banner", "\"ca-app-pub-3940256099942544/9214589741\"")

            // Language/onboarding flow ads
            buildConfigField("String", "_201_lfo_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_202_lfo_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_202_lfo_native_max", "\"3356bd52e474d1b0\"")

            // Onboarding ads
            buildConfigField("String", "_301_onb_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_302_onb_native_max", "\"b732374a53ae7045\"")
            buildConfigField("String", "_302_onb_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_304_onb_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_305_onb_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_306_onb_inter", "\"ca-app-pub-3940256099942544/1033173712\"")

            // Home screen ads
            buildConfigField("String", "_401_home_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_402_click_inter", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "_403_click_inter", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "_403_resume_open", "\"ca-app-pub-3940256099942544/9257395921\"")

            // ss2 splash
            buildConfigField("String", "_101_v2_spl_inter", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "_102_v2_spl_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_103_v2_spl_banner", "\"ca-app-pub-3940256099942544/9214589741\"")
            buildConfigField("String", "_101_v2_spl_inter_high", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "_102_v2_spl_native_high", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_103_v2_spl_banner_high", "\"ca-app-pub-3940256099942544/6300978111\"")

            buildConfigField("String", "_101_spl_inter_high", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "_102_spl_native_high", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_103_spl_banner_high", "\"ca-app-pub-3940256099942544/6300978111\"")
            buildConfigField("String", "_201_lfo_native_high", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_202_lfo_native_high", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_301_onb_native_high", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_302_onb_native_high", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_304_onb_native_high", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_305_onb_native_high", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_306_onb_inter_high", "\"ca-app-pub-3940256099942544/1033173712\"")

            // Alert settings ads
            buildConfigField("String", "_501_alertsetting_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_502_clap2find_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_503_antithief_native", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "_504_antitouch_native", "\"ca-app-pub-3940256099942544/2247696110\"")

            buildConfigField("Boolean", "env_dev", "true")
        }

        create("appProd") {
            // Production ad IDs
            manifestPlaceholders["ad_app_id"] = "ca-app-pub-6513207251178678~6941439837"
            buildConfigField("String", "key_max", "\"pmaJXAUhzJmbd_UelyvIXNuTfnk5SrmGRbENaSg47iJv8ETOKgTGVbrihsAOVaIF8csgk6LbGUf51PA5HuMpBO\"")

            // Splash ads
            buildConfigField("String", "_101_spl_inter", "\"ca-app-pub-6513207251178678/8718266371\"")
            buildConfigField("String", "_102_spl_native", "\"ca-app-pub-6513207251178678/3936002544\"")
            buildConfigField("String", "_103_spl_banner", "\"ca-app-pub-6513207251178678/2622920879\"")

            // Language/onboarding flow ads
            buildConfigField("String", "_201_lfo_native", "\"ca-app-pub-6513207251178678/4155478483\"")
            buildConfigField("String", "_202_lfo_native", "\"ca-app-pub-6513207251178678/3848356725\"")
            buildConfigField("String", "_202_lfo_native_max", "\"69c0fe77165e3c5f\"")

            // Onboarding ads
            buildConfigField("String", "_301_onb_native", "\"ca-app-pub-6513207251178678/2842396818\"")
            buildConfigField("String", "_302_onb_native", "\"ca-app-pub-6513207251178678/1573916679\"")
            buildConfigField("String", "_302_onb_native_max", "\"61c38811f8200282\"")
            buildConfigField("String", "_304_onb_native", "\"ca-app-pub-6513207251178678/7324567300\"")
            buildConfigField("String", "_305_onb_native", "\"ca-app-pub-6513207251178678/1529315145\"")
            buildConfigField("String", "_306_onb_inter", "\"ca-app-pub-6513207251178678/6478405405\"")

            // Home screen ads
            buildConfigField("String", "_401_home_native", "\"ca-app-pub-6513207251178678/8841555304\"")
            buildConfigField("String", "_402_click_inter", "\"ca-app-pub-6513207251178678/1465735967\"")
            buildConfigField("String", "_403_resume_open", "\"ca-app-pub-6513207251178678/1222193389\"")

            // ss2 splash
            buildConfigField("String", "_101_v2_spl_inter", "\"ca-app-pub-6513207251178678/6011485635\"")
            buildConfigField("String", "_102_v2_spl_native", "\"ca-app-pub-6513207251178678/9216233479\"")
            buildConfigField("String", "_103_v2_spl_banner", "\"ca-app-pub-6513207251178678/7528473632\"")

            // Alert settings ads
            buildConfigField("String", "_501_alertsetting_native", "\"ca-app-pub-6513207251178678/8166556820\"")
            buildConfigField("String", "_502_clap2find_native", "\"ca-app-pub-6513207251178678/8598829333\"")
            buildConfigField("String", "_503_antithief_native", "\"ca-app-pub-6513207251178678/4227311817\"")
            buildConfigField("String", "_504_antitouch_native", "\"ca-app-pub-6513207251178678/1274232457\"")

            // High eCPM ads
            buildConfigField("String", "_101_spl_inter_high", "\"ca-app-pub-6513207251178678/4176029281\"")
            buildConfigField("String", "_102_spl_native_high", "\"ca-app-pub-6513207251178678/8858985109\"")
            buildConfigField("String", "_103_spl_banner_high", "\"ca-app-pub-6513207251178678/3237965099\"")
            buildConfigField("String", "_201_lfo_native_high", "\"ca-app-pub-6513207251178678/6089351539\"")
            buildConfigField("String", "_202_lfo_native_high", "\"ca-app-pub-6513207251178678/1757362468\"")
            buildConfigField("String", "_301_onb_native_high", "\"ca-app-pub-6513207251178678/4776269863\"")
            buildConfigField("String", "_302_onb_native_high", "\"ca-app-pub-6513207251178678/9444280795\"")
            buildConfigField("String", "_304_onb_native_high", "\"ca-app-pub-6513207251178678/9815368779\"")
            buildConfigField("String", "_305_onb_native_high", "\"ca-app-pub-6513207251178678/4563042091\"")
            buildConfigField("String", "_306_onb_inter_high", "\"ca-app-pub-6513207251178678/3249960423\"")
            buildConfigField("String", "_101_v2_spl_inter_high", "\"ca-app-pub-6513207251178678/2759005831\"")
            buildConfigField("String", "_102_v2_spl_native_high", "\"ca-app-pub-6513207251178678/6612095716\"")
            buildConfigField("String", "_103_v2_spl_banner_high", "\"ca-app-pub-6513207251178678/1154727230\"")

            buildConfigField("String", "mail_app_key", "\"${project.findProperty("MAIL_APP_KEY_PROD")}\"")
            buildConfigField("String", "mail_app_address", "\"${project.findProperty("MAIL_APP_ADDRESS_PROD")}\"")
            buildConfigField("Boolean", "env_dev", "false")
        }
    }
    lint {
        disable += listOf("ContentDescription", "SpUsage", "UselessParent")
    }

    applicationVariants.all {
        val variant = this
        variant.outputs.all {
            // Check if it's APK file
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                val timestamp = System.currentTimeMillis() / 1000
                outputFileName = "clapfindphone_${variant.flavorName}_${variant.buildType.name}_${variant.versionCode}_${timestamp}.apk"
            }
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    implementation(project(":libbase_obd"))
    implementation(fileTree(mapOf("dir" to "../libbase_obd/libs", "include" to listOf("*.aar"))))
    // Core Android - Updated versions from translate
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.multidex:multidex:2.0.1")

    // Compose - Added from translate
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.ui:ui-graphics:1.5.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("com.google.dagger:hilt-android:2.47")

    // Google Play Services
    implementation("com.google.android.play:review-ktx:2.0.1")
    implementation("com.google.android.gms:play-services-ads:24.5.0")
    implementation("com.google.android.play:review:2.0.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.4.1")
    implementation("com.google.firebase:firebase-crashlytics:18.4.1")
    implementation("com.google.firebase:firebase-analytics:21.3.0")
    implementation("com.google.firebase:firebase-messaging:24.0.1")
    implementation("com.google.firebase:firebase-config:21.1.1")
    implementation("com.google.firebase:firebase-database:20.2.2")

    // UI Libraries
    implementation("com.github.bumptech.glide:glide:5.0.0-rc01")
    implementation("com.airbnb.android:lottie:6.4.1")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.github.skydoves:powermenu:2.2.0")
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
    implementation("com.github.yangmbin:RatingBar:1.0.0")
    implementation("com.intuit.sdp:sdp-android:1.1.1")

    // Networking & Data
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.12")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

    // Localization
    implementation("com.akexorcist:localization:1.2.10") {
        exclude(group = "androidx.core", module = "core")
    }

    // Permissions
    implementation("com.karumi:dexter:6.2.3")

    // Facebook
    implementation("androidx.annotation:annotation:1.0.0")
    implementation("com.facebook.android:audience-network-sdk:6.17.0")
    implementation("com.google.ads.mediation:facebook:6.20.0.1")

    // Lifecycle components
    implementation("android.arch.lifecycle:extensions:1.1.1")
    annotationProcessor("android.arch.lifecycle:compiler:1.1.1")

    // Room
    implementation("androidx.room:room-ktx:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")

    // Handle Record Sound
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation(files("libs/TarsosDSP-1.7.jar"))

    // Ad Mediation - Updated versions from translate
    implementation("com.google.ads.mediation:facebook:6.20.0.1")
    implementation("com.google.ads.mediation:applovin:13.4.0.0")
    implementation("com.google.ads.mediation:vungle:7.5.1.0")
    implementation("com.google.ads.mediation:pangle:7.6.0.3.0")
    implementation("com.google.ads.mediation:mintegral:16.9.91.1")

    // Additional mediation from translate
    implementation("com.unity3d.ads:unity-ads:4.16.1")
    implementation("com.google.ads.mediation:unity:4.16.1.0")
    implementation("com.google.ads.mediation:vungle:7.5.1.0")

    // Mediation MAX from translate
    implementation("com.applovin:applovin-sdk:13.4.0")
    implementation("com.applovin.mediation:google-adapter:24.5.0.0")
    implementation("com.applovin.mediation:facebook-adapter:6.20.0.0")
    implementation("com.applovin.mediation:bytedance-adapter:7.6.0.3.0")
    implementation("com.applovin.mediation:mintegral-adapter:16.9.91.0")
    implementation("com.applovin.mediation:unityads-adapter:4.16.1.0")
    implementation("com.facebook.android:facebook-android-sdk:latest.release")

    // Additional libraries from translate
    implementation("io.airbridge:sdk-android:4.7.1")
    implementation("com.google.android.ump:user-messaging-platform:3.0.0")
    implementation("androidx.navigation:navigation-fragment:2.8.5")
    implementation("androidx.navigation:navigation-ui:2.8.5")
    implementation("com.tbuonomo:dotsindicator:5.1.0")

    // Networking & JSON parsing from translate
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.android.gms:play-services-vision:18.0.0")

    // Project modules
    implementation(project(":custom_seekbar"))

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")
}