rootProject.name = "IDAV50"
include(":app")
include(":common")
include(":resource")
include(":core")
include(":config")
include(":data")
include(":fcm")
include(":data:network")
include(":data:offline")
include(":data:provider")
include(":features:ondemand:video")
include(":features:ondemand:chat")
include(":features:ondemand:mediator")
include(":features:installer")

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
        maven { url = uri("https://jitpack.io") }
    }
}


