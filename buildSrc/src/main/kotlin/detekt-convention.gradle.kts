plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    // Specify that standard Kotlin sources should be used
    source.setFrom(files(project.projectDir.absolutePath))

    // We will use the default configuration but extend it.
    // This is a best practice to avoid losing default rule updates.
    buildUponDefaultConfig = true

    // Include only stable rules.
    // allRules = true might enable experimental or controversial rules.
    allRules = false

    // Path to our custom configuration file.
    // We will create it later when we want to change something.
    config.setFrom(files(project.rootProject.file("config/detekt/detekt.yml")))
}

dependencies {
    // Dependency required for the plugin to work
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}