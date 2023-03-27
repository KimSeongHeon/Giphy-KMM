plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.example.giphy_kmm.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.example.giphy_kmm.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(AndroidDependencies.Coroutines.COROUTINE_CORE)
    //compose
    implementation(AndroidDependencies.Compose.UI)
    implementation(AndroidDependencies.Compose.ANIMATION)
    implementation(AndroidDependencies.Compose.FOUNDATION)
    implementation(AndroidDependencies.Compose.MATERIAL)
    implementation(AndroidDependencies.Compose.RUNTIME)
    implementation(AndroidDependencies.Compose.RUNTIME_RXJAVA)
    implementation(AndroidDependencies.Compose.RUNTIME_LIVEDATA)
    implementation(AndroidDependencies.Compose.ACTIVITY_COMPOSE)
    implementation(AndroidDependencies.Compose.PREVIEW)
    implementation(AndroidDependencies.Compose.VIEWBINDING)

    //glide
    implementation(AndroidDependencies.Glide.COMPOSE_GLIDE)

    //rxjava
    implementation(AndroidDependencies.RxJava.RX_JAVA)
    implementation(AndroidDependencies.RxJava.RX_ANDROID)


    //ktx
    implementation(AndroidDependencies.KTX.LIFECYCLE)
    implementation(AndroidDependencies.KTX.CORE)
    implementation(AndroidDependencies.KTX.ACTIVITY)
    //lifecycle
    implementation(AndroidDependencies.Lifecycle.VIEWMODEL)

    //hilt
    implementation(AndroidDependencies.DependencyInjection.Hilt.ANDROID)
    kapt(AndroidDependencies.DependencyInjection.Hilt.COMPILER)
}

kapt {
    correctErrorTypes = true

    javacOptions {
        // These options are normally set automatically via the Hilt Gradle plugin, but we
        // set them manually to workaround a bug in the Kotlin 1.5.20
        option("-Adagger.fastInit=ENABLED")
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}
