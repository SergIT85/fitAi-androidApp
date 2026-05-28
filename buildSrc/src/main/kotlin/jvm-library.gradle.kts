plugins {
    id("org.jetbrains.kotlin.jvm")
    id("ktlint-convention")
    id("detekt-convention")
}

// Конфигурация Java и Kotlin для всех JVM модулей
java {
    sourceCompatibility = JavaVersion.toVersion(ProjectConfig.JAVA_VERSION)
    targetCompatibility = JavaVersion.toVersion(ProjectConfig.JAVA_VERSION)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(ProjectConfig.JVM_TARGET))
    }
}

// Здесь же можно добавить зависимости, общие для всех JVM модулей
// dependencies {
//     val libs = versionCatalogs.named("libs")
//     implementation(libs.findLibrary("kotlinx-coroutines-core").get())
//     testImplementation(libs.findLibrary("junit").get())
// }