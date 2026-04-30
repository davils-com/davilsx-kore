@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    com.android.kotlin.multiplatform.library
    org.jetbrains.kotlin.multiplatform
    com.google.devtools.ksp
    com.davils.kreate
    `maven-publish`
    io.kotest
}

kreate {
    project {
        name = "davilsx-kore"
        description = " The core library of the DavilsX ecosystem, providing essential utilities and abstractions for Kotlin Multiplatform projects. "

        docs {
            enabled = true
            moduleName = "Kore"
            outputDirectory = "dokka"
        }

        tests {
            enabled = true
            maxParallelForks = Runtime.getRuntime().availableProcessors()
            ignoreFailures = false
            alwaysRunTests = false
            failOnNoDiscoveredTests = false

            logging {
                logPassedTests = true
                logSkippedTests = true
                logTestStarted = true
            }

            report {
                enabled = true
                xml = true
                html = false
            }
        }

        publish {
            enabled = true
            inceptionYear = 2026
            website = "https://github.com/davils-com/davilsx-kore"

            pom {
                issueManagement {
                    system = "Github Issues"
                    url = "https://github.com/davils-com/davilsx-kore/issues"
                }

                ciManagement {
                    system = "Github Actions"
                    url = "https://github.com/davils-com/davilsx-kore/actions"
                }

                licenses {
                    license {
                        name = "Apache 2.0"
                        url = "https://github.com/davils-com/davilsx-kore/blob/main/LICENSE"
                        distribution = "repo"
                    }
                }

                developers {
                    developer {
                        id = "davils"
                        name = "Davils"
                        email = "development@davils.com"
                        organization = "Davils"
                        timezone = "Europe/Berlin"
                    }
                }

                scm {
                    url = "https://github.com/davils-com/davilsx-kore.git"
                    connection = "scm:git:https://github.com/davils-com/davilsx-kore.git"
                    developerConnection = "scm:git@github.com:davils-com/davilsx-kore.git"
                }
            }

            repositories {
                mavenCentral {
                    enabled = true
                    signPublications = true
                    automaticRelease = true
                }
            }
        }
    }

    platform {
        javaVersion = JavaVersion.VERSION_25
        explicitApi = true
        allWarningsAsErrors = true
    }
}

kotlin {
    jvm()

    wasmJs {
        browser()
    }

    js(IR) {
        browser()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    mingwX64()
    macosArm64()

    linuxX64()
    linuxArm64()

    tvosArm64()
    tvosSimulatorArm64()

    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    watchosDeviceArm64()

    android {
        compileSdk { version = release(36) }
        namespace = "com.davils.kore"
        minSdk = 26
        withJava()

        withHostTest {
            isIncludeAndroidResources = true
        }
    }
}
