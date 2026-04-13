@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    org.jetbrains.kotlin.multiplatform
    com.google.devtools.ksp
    com.davils.kreate
    io.kotest
}

kreate {
    project {
        name = "davilsx-kore"
        description = ""

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
}
