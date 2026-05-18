plugins {
    id("android-feature-library")
}

android {
    namespace = "com.by_korchagin.f_settings"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
}