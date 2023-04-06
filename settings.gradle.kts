pluginManagement {
    //for web : https://github.com/JetBrains/compose-multiplatform/blob/master/tutorials/Web/Getting_Started/README.md
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Giphy-KMM"
include(":androidApp")
include(":shared")
include(":compose_web")
include(":compose_desktop")
