plugins {
    `kore-compliance-security`
    `kore-code-analysis`
    `kore-testing`
    `kore-publish`
    `kore-core`
}

kotlin {
    sourceSets {
        androidHostTest {
            dependencies {
                implementation(libs.bundles.kore.tests.common.impl)
                implementation(libs.bundles.kore.tests.jvm.impl)
            }
        }

        commonMain {
            dependencies {
                implementation(libs.bundles.kore.common.impl)
                api(libs.bundles.kore.common.api)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.bundles.kore.tests.common.impl)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.bundles.kore.tests.jvm.impl)
            }
        }
    }
}