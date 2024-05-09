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
        maven { setUrl("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "RoomE"
include(":presentation")
include(":data")
include(":domain")
