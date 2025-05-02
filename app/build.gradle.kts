import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.devtools.ksp)
}

val FLICKR_API_KEY: String = gradleLocalProperties(rootDir, providers).getProperty("FLICKR_API_KEY")
val FLICKR_SECRET: String = gradleLocalProperties(rootDir, providers).getProperty("FLICKR_SECRET")
val FLICKR_API_BASE_URL: String = gradleLocalProperties(rootDir, providers).getProperty("FLICKR_API_BASE_URL")
val FLICKR_API_CALLBACK: String = gradleLocalProperties(rootDir, providers).getProperty("FLICKR_API_CALLBACK")

android {
    namespace = "com.arbuzerxxl.vibeshot"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.arbuzerxxl.vibeshot"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        android.buildFeatures.buildConfig = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.buildConfig = true

    buildTypes {
        debug {
            buildConfigField("String", "FLICKR_API_KEY", "\"${FLICKR_API_KEY}\"")
            buildConfigField("String", "FLICKR_SECRET", "\"${FLICKR_SECRET}\"")
            buildConfigField("String", "FLICKR_API_BASE_URL", "\"${FLICKR_API_BASE_URL}\"")
            buildConfigField("String", "FLICKR_API_CALLBACK", "\"${FLICKR_API_CALLBACK}\"")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composecompiler.get()
    }
}

dependencies {

    // modules
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:design"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(project(":features:start"))
    implementation(project(":features:auth"))
    implementation(project(":features:bottom_menu"))
    implementation(project(":features:interests"))
    implementation(project(":features:profile"))
    implementation(project(":features:details"))

    // di
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // pagging
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.room.paging)

    // ui compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.ui.text.google.fonts)

    // navigation
    implementation(libs.navigation)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.common.ktx)

    // presentation
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.transformations)

    // data
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.json)
    implementation(libs.converter.scalars)
    implementation(libs.okhttp3.interceptor)
    implementation(libs.gson)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // auth
    implementation(libs.androidx.browser)

    // tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}