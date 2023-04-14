object AndroidDependencies {
    object Coroutines {
        const val COROUTINE_CORE =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE_VERSION}"
    }

    object DependencyInjection {
        object Hilt {
            const val ANDROID = "com.google.dagger:hilt-android:${Versions.HILT_VERSION}"
            const val COMPILER = "com.google.dagger:hilt-android-compiler:${Versions.HILT_VERSION}"
        }
    }

    object Compose {
        //https://developer.android.com/jetpack/androidx/releases/compose?hl=ko
        const val UI = "androidx.compose.ui:ui:${Versions.COMPOSE_UI_VERSION}"
        const val ANIMATION = "androidx.compose.animation:animation:${Versions.COMPOSE_VERSION}"
        const val COMPILER = "androidx.compose.compiler:compiler:${Versions.COMPOSE_VERSION}"
        const val FOUNDATION = "androidx.compose.foundation:foundation:${Versions.COMPOSE_VERSION}"
        const val MATERIAL = "androidx.compose.material:material:${Versions.COMPOSE_VERSION}"
        const val RUNTIME = "androidx.compose.runtime:runtime:${Versions.COMPOSE_VERSION}"
        const val RUNTIME_RXJAVA =
            "androidx.compose.runtime:runtime-rxjava2:${Versions.COMPOSE_VERSION}"
        const val RUNTIME_LIVEDATA =
            "androidx.compose.runtime:runtime-livedata:${Versions.COMPOSE_VERSION}"
        const val ACTIVITY_COMPOSE =
            "androidx.activity:activity-compose:${Versions.COMPOSE_VERSION}"
        const val PREVIEW = "androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE_VERSION}"
        const val VIEWBINDING = "androidx.compose.ui:ui-viewbinding:${Versions.COMPOSE_VERSION}"
        const val NAVIGATION = "androidx.navigation:navigation-compose:${Versions.COMPOSE_NAVIGATION}"
    }

    object Glide {
        const val COMPOSE_GLIDE = "com.github.skydoves:landscapist-glide:${Versions.GLIDE_VIERSION}"
    }

    object RxJava {
        const val RX_JAVA = "io.reactivex.rxjava3:rxjava:${Versions.RX_JAVA}"
        const val RX_ANDROID = "io.reactivex.rxjava3:rxandroid:${Versions.RX_ANDROID}"
    }

    object KTX {
        const val ACTIVITY = "androidx.activity:activity-ktx:${Versions.KTX_ACTIVITY_VERSION}"
        const val CORE = "androidx.core:core-ktx:${Versions.KTX_CORE}"
        const val LIFECYCLE = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
    }

    object Lifecycle {
        const val VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.ARCH_VIEWMODEL}"
    }
}
