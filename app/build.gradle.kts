@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.snow.diary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.snow.diary"
        minSdk = 28
        //noinspection OldTargetApi
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(platform(libs.compose.bom))

    implementation(libs.compose.material3)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.animation)

    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)

    implementation(project(":core:database"))
    implementation(libs.oneui)
}