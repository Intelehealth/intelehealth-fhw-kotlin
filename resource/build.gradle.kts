import extensions.androidTestDeps
import extensions.resourceModuleDeps
import extensions.unitTestDeps

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.detekt.plugin)
    alias(libs.plugins.kotlin.kapt)
}

apply("${rootProject.projectDir}/whitelabel.gradle")
apply("${rootProject.projectDir}/variants.gradle")

android {
    namespace = "org.intelehealth.resource"
    compileSdk = AndroidConfig.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidConfig.MIN_SDK

        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = AndroidConfig.JAVA_VERSION
        targetCompatibility = AndroidConfig.JAVA_VERSION
    }
    kotlinOptions {
        jvmTarget = AndroidConfig.JVM_TARGET
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }


}

dependencies {
    resourceModuleDeps()
    unitTestDeps()
    androidTestDeps()
}
