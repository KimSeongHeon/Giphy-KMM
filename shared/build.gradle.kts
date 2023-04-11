import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.codingfeline.buildkonfig")
}

kotlin {
    //for android
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    //for js
    js(IR) {
        useCommonJs()
        browser()
    }

    //for ios
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    //for desktop
    jvm()
//    macosArm64("macOS")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(SharedDependencies.Coroutines.COROUTINE_CORE)
                implementation(SharedDependencies.DependencyInjection.KOIN.CORE)

                //network
                implementation(SharedDependencies.Network.Ktor.CORE)
                implementation(SharedDependencies.Network.Ktor.JSON)
                implementation(SharedDependencies.Network.Ktor.NEGOTIATION)

                //db
                implementation(SharedDependencies.SqlDelight.RUNTIME)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(SharedDependencies.Network.Ktor.Android.CLIENT)
                implementation(SharedDependencies.Log.LoggerFactory)
                implementation(SharedDependencies.SqlDelight.Android.DRIVER)
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(SharedDependencies.Network.Ktor.iOS.CLIENT)
                implementation(SharedDependencies.SqlDelight.Native.DRIVER)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
        val jsMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(SharedDependencies.Network.Ktor.JS.CLEINT)
                implementation(SharedDependencies.SqlDelight.JS.DRIVER)
            }
        }
        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(SharedDependencies.Network.Ktor.JVM.CLIENT)
                implementation(SharedDependencies.SqlDelight.JVM.DRIVER)
            }
        }
    }
}

android {
    namespace = "com.example.giphy_kmm"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
}

buildkonfig {
    packageName = "com.example.giphy_kmm"

    loadLocalProperties()

    defaultConfigs {
        buildConfigField(
            Type.STRING,
            "GIPHY_API_KEY",
            gradleLocalProperties(rootDir).getProperty("api.key")
        )
    }
}

fun loadLocalProperties() {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
}
