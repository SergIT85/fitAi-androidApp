plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

configureBaseAndroid(extensions.getByType<com.android.build.api.dsl.LibraryExtension>())

dependencies {
    val libs = versionCatalogs.named("libs")

    // ТОЛЬКО критически важные для ЛЮБОГО Android-модуля зависимости
    implementation(libs.findLibrary("androidx-core-ktx").get())

    // Тестовые зависимости - они нужны всем
    testImplementation(libs.findLibrary("junit").get())
    androidTestImplementation(libs.findLibrary("androidx-junit").get())
    androidTestImplementation(libs.findLibrary("androidx-espresso-core").get())
}