import extensions.androidTestDeps
import extensions.featureInstallerModuleDeps
import extensions.unitTestDeps

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin(Plugins.KAPT)
    id(Plugins.PARCELIZE)
    id(Plugins.DAGGER_HILT)
}

apply("${rootProject.projectDir}/whitelabel.gradle")
apply("${rootProject.projectDir}/variants.gradle")

android {
    namespace = "org.intelehealth.installer"
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

    kapt {
        correctErrorTypes = true
//        arguments {
//            arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
//            arg("dagger.hilt.android.internal.projectType", "APP")
//            arg("dagger.hilt.internal.useAggregatingRootProcessor", "true")
//        }
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    featureInstallerModuleDeps()
//    api(project(":common"))
////    api(project(":core:ui"))
//    implementation("com.google.android.play:feature-delivery:2.1.0")
//    implementation("com.google.android.play:review:2.0.2")
//    implementation("androidx.core:core-ktx:1.13.1")
//    implementation("androidx.appcompat:appcompat:1.7.0")
//    implementation("com.google.android.material:material:1.12.0")
    unitTestDeps()
    androidTestDeps()
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.2.1")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}