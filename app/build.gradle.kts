plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "es.davante.edadcanina"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        // Id distinto al de la v2 para poder tener las dos apps instaladas a la vez
        applicationId = "es.davante.edadcanina.mvc"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        // Al compilar, DataBinding genera las clases que enlazan el XML con Java
        // (ActivityMainBinding y BR), así no hay que buscar las vistas a mano
        dataBinding = true
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}