plugins {
    id("com.android.library")
}

configureAndroidLibrary()

dependencies {
    val libs = versionCatalogs.named("libs")

    implementation(libs.findLibrary("androidx-core-ktx").get())
    implementation(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
    implementation(libs.findLibrary("androidx-lifecycle-viewmodel-savedstate").get())

    testImplementation(libs.findLibrary("junit").get())
    androidTestImplementation(libs.findLibrary("androidx-junit").get())
    androidTestImplementation(libs.findLibrary("androidx-espresso-core").get())
}
