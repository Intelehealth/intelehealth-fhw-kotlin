import extensions.androidTestDeps
import extensions.fcmModuleDeps
import extensions.unitTestDeps
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.detekt.plugin)
}

apply("${rootProject.projectDir}/whitelabel.gradle")
apply("${rootProject.projectDir}/variants.gradle")

android {
    namespace = "org.intelehealth.fcm"
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

    buildFeatures { buildConfig = true }
}

dependencies {
    fcmModuleDeps()
    unitTestDeps()
    androidTestDeps()
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
}
tasks.withType<Detekt> {
    config.setFrom(files("$rootDir/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    reports {
        html.required.set(true)
        html.outputLocation.set(file("$rootDir/detekt/reports/fcm.html"))
    }
}
