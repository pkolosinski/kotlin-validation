import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25

plugins {
    `maven-publish`
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.axion.release)
    alias(libs.plugins.kotest)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
}

group = "dev.pkolosinski.kotlinvalidation"
version = scmVersion.version

kotlin {
    jvm {
        compilerOptions {
            jvmTarget = JVM_25
        }
    }
    iosArm64()
    iosSimulatorArm64()
    android {
        namespace = "dev.pkolosinski.kotlinvalidation"
        compileSdk = 36
        minSdk = 33
        compilerOptions {
            jvmTarget = JVM_25
        }
        withHostTest { }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.framework.engine)
            implementation(libs.kotlinx.datetime)
        }
        jvmTest.dependencies {
            implementation(libs.kotest.runner.junit6)
        }
        named("androidHostTest").dependencies {
            implementation(libs.kotest.runner.junit6)
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        exceptionFormat = FULL
    }
}

ktlint {
    filter {
        exclude("**/generated/**")
    }
}

// publishing {
//    publications {
//        register<MavenPublication>("gpr") {
//            from(components["java"])
//        }
//    }
//    repositories {
//        maven {
//            name = "GitHubPackages"
//            url = uri("https://maven.pkg.github.com/pkolosinski/kotlin-validation")
//            credentials {
//                username = System.getenv("GITHUB_ACTOR")
//                password = System.getenv("GITHUB_TOKEN")
//            }
//        }
//    }
// }
