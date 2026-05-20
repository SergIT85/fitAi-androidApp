plugins {
    `kotlin-dsl`
    `version-catalog`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:9.2.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.21")
    implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.3.21")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.3.8")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.59.2")
}