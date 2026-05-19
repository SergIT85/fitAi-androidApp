plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

configureAndroidApplication()

extensions.configure<com.android.build.api.dsl.ApplicationExtension> {
    defaultConfig {
        applicationId = ProjectConfig.APPLICATION_ID
        targetSdk = ProjectConfig.TARGET_SDK
        versionCode = ProjectConfig.VERSION_CODE
        versionName = ProjectConfig.VERSION_NAME
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        named("debug") {
            applicationIdSuffix = ProjectConfig.BuildTypes.Debug.APPLICATION_ID_SUFFIX
            versionNameSuffix = ProjectConfig.BuildTypes.Debug.VERSION_NAME_SUFFIX
            isDebuggable = ProjectConfig.BuildTypes.Debug.IS_DEBUGGABLE
            isMinifyEnabled = ProjectConfig.BuildTypes.Debug.IS_MINIFY_ENABLED
        }

        named("release") {
            isMinifyEnabled = ProjectConfig.BuildTypes.Release.IS_MINIFY_ENABLED
            isShrinkResources = ProjectConfig.BuildTypes.Release.IS_SHRINK_RESOURCES
            isDebuggable = ProjectConfig.BuildTypes.Release.IS_DEBUGGABLE
        }
    }
}

dependencies {
    val libs = versionCatalogs.named("libs")

    // Базовые Android зависимости
    implementation(libs.findLibrary("androidx-core-ktx").get())
    implementation(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())

    // Compose зависимости
    implementation(libs.findLibrary("androidx-activity-compose").get())
    implementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    implementation(libs.findLibrary("androidx-compose-ui").get())
    implementation(libs.findLibrary("androidx-compose-ui-graphics").get())
    implementation(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
    implementation(libs.findLibrary("androidx-compose-material3").get())

    // Тестовые зависимости
    testImplementation(libs.findLibrary("junit").get())
    androidTestImplementation(libs.findLibrary("androidx-junit").get())
    androidTestImplementation(libs.findLibrary("androidx-espresso-core").get())
    androidTestImplementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    androidTestImplementation(libs.findLibrary("androidx-compose-ui-test-junit4").get())

    // Debug зависимости
    debugImplementation(libs.findLibrary("androidx-compose-ui-tooling").get())
    debugImplementation(libs.findLibrary("androidx-compose-ui-test-manifest").get())
}
