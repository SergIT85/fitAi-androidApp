import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Добавляет зависимости для модулей, работающих с навигацией
 */
fun Project.addNavigationDependencies() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        // Когда добавите Navigation в toml, раскомментируйте:
        // add("implementation", libs.findLibrary("androidx.navigation.compose").get())
    }
}

/**
 * Добавляет зависимости для модулей с Room Database
 */
fun Project.addRoomDependencies() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        // add("implementation", libs.findLibrary("androidx.room.runtime").get())
        // add("kapt", libs.findLibrary("androidx.room.compiler").get())
    }
}

/**
 * Добавляет зависимости для DI (Hilt/Koin)
 */
fun Project.addDiDependencies() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        // add("implementation", libs.findLibrary("hilt.android").get())
        // add("kapt", libs.findLibrary("hilt.compiler").get())
    }
}
