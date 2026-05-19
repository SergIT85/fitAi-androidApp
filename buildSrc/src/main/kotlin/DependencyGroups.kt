import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

fun Project.addNavigationDependencies() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        // Когда добавите Navigation в toml, раскомментируйте:
        // add("implementation", libs.findLibrary("androidx.navigation.compose").get())
    }
}

fun Project.addRoomDependencies() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        add("implementation", libs.findLibrary("room-runtime").get())
        add("implementation", libs.findLibrary("room-ktx").get())
        add("ksp", libs.findLibrary("room-compiler").get())
    }
}

fun Project.addDiDependencies() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        add("implementation", libs.findLibrary("dagger-hilt-android").get())
        add("ksp", libs.findLibrary("dagger-hilt-compiler").get())
    }
}
