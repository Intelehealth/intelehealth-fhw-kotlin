import extensions.androidTestDeps
import extensions.configModuleDeps
import extensions.unitTestDeps

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.detekt.plugin)
}

apply("${rootProject.projectDir}/whitelabel.gradle")
apply("${rootProject.projectDir}/variants.gradle")

android {
    namespace = "org.intelehealth.config"
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
        buildConfig = true
    }

    kapt {
        correctErrorTypes = true
//        arguments {
//            arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
//            arg("dagger.hilt.android.internal.projectType", "APP")
//            arg("dagger.hilt.internal.useAggregatingRootProcessor", "true")
//        }
    }
}

dependencies {

    configModuleDeps()
    unitTestDeps()
    androidTestDeps()
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
}
