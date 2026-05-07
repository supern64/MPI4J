plugins {
    id("java")
}

group = "me.cirnoslab.mpiclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))
}
