object ProjectConfig {
    const val COMPILE_SDK = 36
    const val MIN_SDK = 24
    const val TARGET_SDK = 36

    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"

    const val APPLICATION_ID = "com.by_korchagin.fitai"

    const val JAVA_VERSION = 21
    const val JVM_TARGET = "21"

    object BuildTypes {
        object Debug {
            const val APPLICATION_ID_SUFFIX = ".debug"
            const val VERSION_NAME_SUFFIX = "-DEBUG"
            const val IS_MINIFY_ENABLED = false
            const val IS_DEBUGGABLE = true
        }

        object Release {
            const val IS_MINIFY_ENABLED = true
            const val IS_SHRINK_RESOURCES = true
            const val IS_DEBUGGABLE = false
        }
    }
}