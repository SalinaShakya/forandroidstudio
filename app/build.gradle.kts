plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)

    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.myview"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.myview"
        minSdk = 24
        targetSdk = 36
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
        viewBinding = true
        compose = true

    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}



dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)


    implementation(libs.retrofit)
    implementation(libs.converter.gson)
//    implementation("com.squareup.retrofit2:retrofit:2.10.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.10.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")


    implementation(libs.glide)
//    ksp(libs.compiler)
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)

    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation("androidx.fragment:fragment-ktx:1.8.5")

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(platform("androidx.compose:compose-bom:2024.12.01"))

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.10.0")

    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

}
