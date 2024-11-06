
val kotlin_version: String by project
val logback_version: String by project
val kotlinx_html_version: String by project

plugins {
    kotlin("jvm") version "2.0.21"
    id("io.ktor.plugin") version "3.0.0"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-html-builder-jvm")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinx_html_version")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1") // For JSON serialization
    //implementation("org.xerial:sqlite-jdbc:3.40.1.0") // SQLite JDBC driver
    implementation("org.jetbrains.exposed:exposed-core:0.41.1") // Exposed core
    //implementation("org.jetbrains.exposed:exposed-dao:0.41.1") // Exposed DAO
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1") // Exposed JDBC

    //implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.h2database:h2:2.2.224")
    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}
