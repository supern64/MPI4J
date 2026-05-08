plugins {
    id("java-library")
}

group = "me.cirnoslab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.2")
    compileOnly("org.jetbrains:annotations:26.1.0")
}
