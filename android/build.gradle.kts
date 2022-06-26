plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("android")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("$rootDir/keystore.jks")
            storePassword = "wverdese"
            keyAlias = "wverdese"
            keyPassword = "wverdese"
        }
    }
    compileSdk = 32
    defaultConfig {
        applicationId = "me.wverdese.proseccheria.android"
        minSdk = 24
        targetSdk = 32
        versionCode = 4
        versionName = "2.0.0"
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isDebuggable = false
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
    implementation("androidx.navigation:navigation-compose:${Version.Compose.NAVIGATION}")
    implementation("androidx.compose.ui:ui:${Version.Compose.MAIN}")
    implementation("androidx.compose.ui:ui-tooling:${Version.Compose.MAIN}")
    /* Firebase */
    implementation("com.google.firebase:firebase-common-ktx:${Version.Firebase.COMMON_KTX}")
    implementation("com.google.firebase:firebase-crashlytics-ktx:${Version.Firebase.CRASHLYTICS_KTX}")
    implementation("com.google.firebase:firebase-analytics-ktx:${Version.Firebase.ANALYTICS_KTX}")
    /* Koin */
    implementation("io.insert-koin:koin-core:${Version.KOIN}")
    implementation("io.insert-koin:koin-androidx-compose:${Version.KOIN}")
    /* KotlinX */
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Version.KotlinX.DATETIME}")
}

object Version {
    const val KOIN = "3.2.0"
    const val FIREBASE = "30.1.0"

    object AndroidX {
        const val ACTIVITY_COMPOSE = "1.3.1"
    }

    object Compose {
        const val MAIN = "1.1.0"
        const val MATERIAL = "1.2.0-alpha03"
        const val NAVIGATION = "2.4.2"
    }

    object Firebase {
        const val COMMON_KTX = "20.0.0"
        const val CRASHLYTICS_KTX = "18.2.11"
        const val ANALYTICS_KTX = "21.0.0"
    }

    object KotlinX {
        const val DATETIME = "0.4.0"
    }
}
