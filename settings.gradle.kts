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
        maven("https://jitpack.io")
    }
}

rootProject.name = "VibeShot"
include(":features:auth")

include(":app")
include(":core:navigation")
include(":core:design")
include(":core:ui")
include(":data")
include(":domain")


include(":features:start")
include(":features:interests")
include(":features:bottom_menu")
include(":features:profile")
include(":features:details")
include(":features:search")
