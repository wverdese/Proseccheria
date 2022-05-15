plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../ios/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                /* JSON */
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
                /* Koin */
                implementation("io.insert-koin:koin-core:3.2.0")
                /* KotlinX */
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1-native-mt")
                /* Settings */
                implementation("com.russhwolf:multiplatform-settings:0.9")
                implementation("com.russhwolf:multiplatform-settings-coroutines-native-mt:0.9")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                /* Koin */
                implementation("com.russhwolf:multiplatform-settings-test:0.9")
                /* KotlinX */
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1-native-mt")
                /* Settings */
                implementation("io.insert-koin:koin-test:3.2.0")
                /* Turbine */
                implementation("app.cash.turbine:turbine:0.8.0")
            }
        }
        val androidMain by getting {
            dependencies {
                /* DataStore */
                implementation("androidx.datastore:datastore:1.0.0")
                implementation("androidx.datastore:datastore-preferences:1.0.0")
                /* DataStore Settings */
                implementation("com.russhwolf:multiplatform-settings-datastore:0.9")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 32
    }
}
