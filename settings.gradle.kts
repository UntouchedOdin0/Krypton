enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        maven("https://repo.kryptonmc.org/releases")
        maven("https://repo.velocitypowered.com/snapshots/")
        jcenter() // Dokka requires this for kotlinx.html
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.kryptonmc.org/snapshots")
        maven("https://jitpack.io")
        mavenCentral()
    }
    versionCatalogs {
        create("global") {
            from(files("gradle/global.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "krypton"

include("api")
include("server")
include("generators")
include("annotation-processor")
