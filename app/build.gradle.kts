plugins {
    id("android-application")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.by_korchagin.fitai"
}

dependencies {

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    implementation(project(":feature:onboarding"))
    implementation(project(":feature:workout"))
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:nutrition"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:ai-coach"))
    implementation(project(":core:navigation"))
    implementation(project(":core:domain"))
}
