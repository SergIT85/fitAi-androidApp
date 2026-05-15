plugins {
    id("android-base-library")
    id("org.jetbrains.kotlin.plugin.compose")
}

extensions.configure<com.android.build.api.dsl.LibraryExtension> {
    buildFeatures {
        compose = true
    }
}

dependencies {
    //  libs
    val libs = versionCatalogs.named("libs")

    implementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    implementation(libs.findLibrary("androidx-compose-ui").get())
    implementation(libs.findLibrary("androidx-compose-ui-graphics").get())
    implementation(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
    implementation(libs.findLibrary("androidx-compose-material3").get())
}