plugins {
    `kore-core`
}

kotlin {
    sourceSets {
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