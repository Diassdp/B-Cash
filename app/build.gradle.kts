plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.example.bcash"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bcash"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://backend-y2k4wvszia-et.a.run.app/\"")
        }

        debug {
            buildConfigField("String", "BASE_URL", "\"https://backend-y2k4wvszia-et.a.run.app/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

}

dependencies {
    // Core libraries
    implementation(libs.androidx.core.ktx) // Kotlin extensions for core Android functionality
    implementation(libs.androidx.appcompat) // AppCompat library for backward compatibility
    implementation(libs.material) // Material Design components
    implementation(libs.androidx.activity) // Activity library for managing the activity lifecycle
    implementation(libs.androidx.constraintlayout) // ConstraintLayout for flexible layouts
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation(libs.androidx.paging.runtime.ktx)

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")

    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Testing
    testImplementation(libs.junit) // Unit testing framework
    androidTestImplementation(libs.androidx.junit) // AndroidX JUnit extension for Android tests
    androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing

    // Networking
    implementation("com.loopj.android:android-async-http:1.4.11") // For asynchronous HTTP requests
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // For REST API communication
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // For converting JSON responses

    // UI and Navigation
    implementation("androidx.viewpager2:viewpager2:1.0.0") // For ViewPager2 functionality
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4") // For fragment-based navigation
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4") // For navigation UI components

    // Image loading and caching
    implementation("com.github.bumptech.glide:glide:4.16.0") // Image loading library

    // Logging
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0") // For logging HTTP requests and responses

    // Circular image views
    implementation("de.hdodenhof:circleimageview:3.1.0") // For circular image views

    // Splash screen
    implementation("androidx.core:core-splashscreen:1.0.0") // For splash screen support

    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Room database
    implementation("androidx.room:room-runtime:2.5.2") // Room database for local storage
    ksp("androidx.room:room-compiler:2.5.2") // Room compiler for generating database code

    // DataStore preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0") // For storing key-value pairs

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2") // Coroutines for asynchronous programming
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2") // Coroutines for Android

    implementation("com.github.yalantis:ucrop:2.2.8")
}