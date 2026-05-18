plugins {
    id("android-base-library")
}

android {
    namespace = "com.by_korchagin.data.repository"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":data:model"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
}