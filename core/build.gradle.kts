plugins {
    id("java")
    kotlin("jvm")
    `maven-publish`
    signing
}

group = "io.kotgres"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

kotlin {
    jvmToolchain(11)
}

java {
    withSourcesJar()
    withJavadocJar()
}

/**
 * MAVEN PUBLISHING (TODO)
 */
