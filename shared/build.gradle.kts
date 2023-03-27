import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.21"
    id("com.android.library")
    id("com.codingfeline.buildkonfig")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Coroutines.COROUTINE_CORE)
                implementation(Dependencies.DependencyInjection.KOIN.CORE)

                //network
                implementation(Dependencies.Network.Ktor.CORE)
                implementation(Dependencies.Network.Ktor.JSON)
                implementation(Dependencies.Network.Ktor.NEGOTIATION)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Network.Ktor.Android.CLIENT)
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
                implementation(Dependencies.Network.Ktor.iOS.CLIENT)
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
            properties["api.key"].toString()
        )
    }
}

fun loadLocalProperties() {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
}
