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

    js(IR) {
        browser()
    }
}
