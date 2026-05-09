plugins {
    id("java-library")
}

group = "me.cirnoslab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.2")
    compileOnly("org.jetbrains:annotations:26.1.0")
}
