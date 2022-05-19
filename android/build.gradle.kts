plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "me.wverdese.proseccheria.android"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.Compose.MAIN
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }
}

dependencies {
    implementation(project(":shared"))
    /* AndroidX */
    implementation("androidx.activity:activity-compose:${Version.AndroidX.ACTIVITY_COMPOSE}")
    /* Compose */
    implementation("androidx.compose.material:material:${Version.Compose.MATERIAL}")
    implementation("androidx.compose.ui:ui:${Version.Compose.MAIN}")
    implementation("androidx.compose.ui:ui-tooling:${Version.Compose.MAIN}")
    /* Koin */
    implementation("io.insert-koin:koin-core:${Version.KOIN}")
    implementation("io.insert-koin:koin-androidx-compose:${Version.KOIN}")
}

object Version {
    const val KOIN = "3.2.0"

    object AndroidX {
        const val ACTIVITY_COMPOSE = "1.3.1"
    }

    object Compose {
        const val MAIN = "1.1.0"
        const val MATERIAL = "1.2.0-alpha03"
    }
}
