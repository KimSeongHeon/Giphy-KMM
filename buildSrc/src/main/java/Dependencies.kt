object Dependencies {
    object Coroutines {
        const val COROUTINE_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE_VERSION}"
    }

    object DependencyInjection {
        object KOIN {
            const val CORE = "io.insert-koin:koin-core:${Versions.KOIN_VERSION}"
            const val TEST = "io.insert-koin:koin-test:${Versions.KOIN_VERSION}"
            const val ANDROID = "io.insert-koin:koin-android:${Versions.KOIN_VERSION}"
        }

    }
}
