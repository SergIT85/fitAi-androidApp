plugins {
    id("org.jlleitschuh.gradle.ktlint")
}

// Ktlint plugin configuration
ktlint {
    version.set("1.2.1") // The version of the Ktlint tool itself
    android.set(true) // Enable support for Android projects
    ignoreFailures.set(false) // The build will fail on linter errors
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}