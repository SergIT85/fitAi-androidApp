plugins {
    id("android-application")
}

android {
    namespace = "com.by_korchagin.fitai"
}

dependencies {
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:workout"))
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:nutrition"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:ai-coach"))
    implementation(project(":core:navigation"))
    implementation(project(":core:domain"))
}
