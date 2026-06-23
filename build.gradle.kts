plugins {
    kotlin("jvm") version "2.4.0"
}

group = "dev.pkolosinski"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("io.kotest:kotest-bom:6.2.1"))
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-runner-junit6")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}
