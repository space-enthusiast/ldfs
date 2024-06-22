pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("jvm").version(extra["kotlin.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
    }
}
rootProject.name = "ldfs-multi"
include("subproject1", "subproject2")
include("ldfs-master")
include("ldfs-chunk")
include("ldfs-client")