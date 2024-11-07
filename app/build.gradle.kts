plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.yandex.todolist"
    compileSdk = 35 // Временно установите на 34, так как SDK 35 нестабилен

    defaultConfig {
        applicationId = "com.yandex.todolist"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)


    // Jetpack Compose Dependencies
    implementation(libs.androidx.ui)

    implementation ("androidx.compose.material3:material3:1.3.1")
    implementation ("androidx.compose.ui:ui:1.7.2")
    implementation(libs.androidx.material3)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.compose.material:material-icons-extended:1.7.5") // Убедитесь, что версия соответствует вашей версии Compose
    implementation("androidx.compose.foundation:foundation:1.7.5")
    implementation ("androidx.compose.runtime:runtime-livedata:1.7.5")


    // Уберите эту строку, если она не нужна:
    // implementation(libs.play.services.gcm) // Не используется для навигации в Compose

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.5")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.5")
}
