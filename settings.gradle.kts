pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Samsung Health SDK (samsung-health-data-api-1.1.0.aar) is not on a public Maven repo.
        // Place the AAR in android/libs/ and uncomment the implementation line in app/build.gradle.kts
    }
}

rootProject.name = "HealthSync"
include(":app")
