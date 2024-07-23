import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

val properties = gradleLocalProperties(rootDir, providers)

android {
    namespace = "com.sevenstars.roome"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sevenstars.roome"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "KAKAO_NATIVE_KEY", "\"${properties.getProperty("KAKAO_NATIVE_KEY")}\"")
        manifestPlaceholders["KAKAO_NATIVE_KEY"] = properties.getProperty("KAKAO_NATIVE_KEY")

        buildConfigField("String", "BASE_URL", "\"${properties.getProperty("BASE_URL")}\"")
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${properties.getProperty("GOOGLE_CLIENT_ID")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kapt {
        correctErrorTypes = true
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val HILT_VER = "2.51.1"
    implementation("com.google.dagger:hilt-android:$HILT_VER")
    kapt("com.google.dagger:hilt-compiler:$HILT_VER")

    val RETROFIT_VER = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$RETROFIT_VER")
    implementation("com.squareup.retrofit2:converter-gson:$RETROFIT_VER")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.orhanobut:logger:2.2.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.7.0")

    implementation("com.kakao.sdk:v2-user:2.20.1")
    implementation("com.kakao.sdk:v2-share:2.19.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation("com.airbnb.android:lottie:6.1.0")

    implementation("com.google.firebase:firebase-analytics:22.0.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.google.android.play:app-update-ktx:2.0.1")
}