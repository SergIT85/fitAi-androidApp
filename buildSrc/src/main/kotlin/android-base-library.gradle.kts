plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

extensions.configure<com.android.build.api.dsl.LibraryExtension> {
    configureBaseAndroid(project)
}

dependencies {
    val libs = versionCatalogs.named("libs")

    implementation(libs.findLibrary("androidx-core-ktx").get())
}