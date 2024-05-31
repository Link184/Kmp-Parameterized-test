plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        jvmMain {
            dependencies {
                implementation(libs.devtoolsKsp.processing.api)
                implementation(libs.kotlin.poet)
                implementation(libs.kotlin.poet.ksp)
            }

            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
        }
    }
}
