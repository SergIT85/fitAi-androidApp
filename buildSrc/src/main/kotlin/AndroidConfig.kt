import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.configureBaseAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        compileSdk = ProjectConfig.COMPILE_SDK // Из центрального файла!

        defaultConfig {
            minSdk = ProjectConfig.MIN_SDK // Из центрального файла!
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            if (this is com.android.build.api.dsl.LibraryDefaultConfig) {
                consumerProguardFiles("consumer-rules.pro")
            }
        }

        buildTypes {
            // Release конфигурация
            named("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }

            // Debug конфигурация
            named("debug") {
                isMinifyEnabled = false
                // Можно добавить debug-специфичные настройки
                // applicationIdSuffix = ".debug" // только для app
                // versionNameSuffix = "-DEBUG"
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.toVersion(ProjectConfig.JAVA_VERSION)
            targetCompatibility = JavaVersion.toVersion(ProjectConfig.JAVA_VERSION)
        }
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(ProjectConfig.JVM_TARGET))
        }
    }
}