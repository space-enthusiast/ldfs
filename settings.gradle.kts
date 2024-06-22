plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "ldfs-multi"
include("subproject1", "subproject2")
include("ldfs-master")
include("ldfs-chunk")
