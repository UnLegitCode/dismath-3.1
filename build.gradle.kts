plugins {
    id("java")
}

group = "ru.unlegit"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("it.unimi.dsi:fastutil:8.5.15")
}