@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "com.github.nabin0.musicduniya"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.nabin0.musicduniya"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

     // Dagger Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.dagger.hilt.android.compiler)
    implementation(libs.dagger.hilt.navigation)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Coroutines
    implementation(libs.coroutine.core)
    implementation(libs.coroutine.android)

    // Retrofit
    implementation(libs.retrofit2)
    implementation(libs.gson)

    // View model and Lifecycle
    implementation(libs.viewmodel.ktx)
    implementation(libs.viewmodel.compose)

    // Material Icons
    implementation(libs.material.icons.extended)

    // Audio player library
    implementation(libs.audio.player.library)

    //Coil
    implementation(libs.coil.compose)

    implementation(libs.androidx.lifecycle.process)


}