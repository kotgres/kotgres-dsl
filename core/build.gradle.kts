plugins {
    id("java")
    kotlin("jvm")
    `maven-publish`
}

group = "io.kotgres"
version = "0.1.2"

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
 * JITPACK PUBLISHING
 */
afterEvaluate {
    publishing {
        publications {
            create("java", MavenPublication::class) {
                from(components["java"])

                groupId = "io.kotgres"
                artifactId = "dsl"
                version = "0.1.2"
            }
        }
    }
}


/**
 * MAVEN PUBLISHING (TODO)
 */
