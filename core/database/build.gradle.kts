plugins {
    id("android-base-library")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.by_korchagin.database"
}

addRoomDependencies()

dependencies {
}
