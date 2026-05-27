plugins {
    id("android-base-library") // Наследует core-ktx и тесты
    id("org.jetbrains.kotlin.plugin.compose")
    id("ktlint-convention")
}

extensions.configure<com.android.build.api.dsl.LibraryExtension> {
    buildFeatures {
        compose = true
    }
}

dependencies {
    val libs = versionCatalogs.named("libs")

    // Compose-зависимости (нужны всем feature-модулям)
    implementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    implementation(libs.findLibrary("androidx-compose-ui").get())
    implementation(libs.findLibrary("androidx-compose-ui-graphics").get())
    implementation(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
    implementation(libs.findLibrary("androidx-compose-material3").get())

    // Базовые UI-библиотеки для feature-модулей
    implementation(libs.findLibrary("androidx-appcompat").get())
    implementation(libs.findLibrary("material").get())
    implementation(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())

    // Debug-зависимости для Compose
    debugImplementation(libs.findLibrary("androidx-compose-ui-tooling").get())
}