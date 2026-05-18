plugins {
    id("android-feature-library")
}

android {
    namespace = "com.by_korchagin.f_ai_coach"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
}