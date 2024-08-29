plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.valenpatel.textrecognization"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.valenpatel.textrecognization"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Google ML Text Recognization Dependency
        // To recognize Latin script
        implementation ("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
        // To recognize Chinese script
        implementation ("com.google.android.gms:play-services-mlkit-text-recognition-chinese:16.0.0")
        // To recognize Devanagari script
        implementation ("com.google.android.gms:play-services-mlkit-text-recognition-devanagari:16.0.0")
        // To recognize Japanese script
        implementation ("com.google.android.gms:play-services-mlkit-text-recognition-japanese:16.0.0")
        // To recognize Korean script
        implementation ("com.google.android.gms:play-services-mlkit-text-recognition-korean:16.0.0")


    dependencies {
        // ViewModel
        implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")

        // Fragment KTX
        implementation ("androidx.fragment:fragment-ktx:1.8.2")
    }


}