plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.4.1").apply(false)
    id("com.android.library").version("7.4.1").apply(false)
    kotlin("android").version("1.8.0").apply(false)
    kotlin("multiplatform").version("1.8.0").apply(false)
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false

    //sqldelight
    id("com.squareup.sqldelight") version "1.5.5" apply false

    //for web : https://github.com/JetBrains/compose-multiplatform/blob/master/tutorials/Web/Getting_Started/README.md
    id("org.jetbrains.compose") version "1.3.1" apply false
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.7.0")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
    }
}

//tasks.register("clean", Delete::class) {
//    delete(rootProject.buildDir)
//}
