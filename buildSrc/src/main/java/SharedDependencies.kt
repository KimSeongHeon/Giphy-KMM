object SharedDependencies {
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

    object Network {
        object Ktor {
            const val CORE = "io.ktor:ktor-client-core:${Versions.KTOR_VESRION}"
            const val NEGOTIATION= "io.ktor:ktor-client-content-negotiation:${Versions.KTOR_VESRION}"
            const val JSON = "io.ktor:ktor-serialization-kotlinx-json:${Versions.KTOR_VESRION}"
            object Android {
                const val CLIENT = "io.ktor:ktor-client-android:${Versions.KTOR_VESRION}"
            }

            object iOS {
                const val CLIENT = "io.ktor:ktor-client-ios:${Versions.KTOR_VESRION}"
            }
        }
    }

    object Log {
        const val LoggerFactory = "com.truthbean.logger:slf4j:0.0.1-RELEASE"
    }
}
