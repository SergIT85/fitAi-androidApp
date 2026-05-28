pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "FitAi"
include(":app")
include(":core:common")
include(":core:domain")
include(":feature:onboarding")
include(":feature:workout")
include(":feature:dashboard")
include(":feature:nutrition")
include(":feature:settings")
include(":feature:ai-coach")
include(":data:repository")
include(":core:network")
include(":core:ui")
include(":core:database")
include(":core:datastore")
include(":core:navigation")
include(":core:testing")
include(":data:model")
