import extensions.androidTestDeps
import extensions.featureOnDemandMediatorModuleDeps
import extensions.unitTestDeps
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.detekt.plugin)
}

apply("${rootProject.projectDir}/whitelabel.gradle")
apply("${rootProject.projectDir}/variants.gradle")

android {
    namespace = "org.intelehealth.features.ondemand.mediator"
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
    featureOnDemandMediatorModuleDeps()
    unitTestDeps()
    androidTestDeps()
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.2.1")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
tasks.withType<Detekt> {
    config.setFrom(files("$rootDir/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    reports {
        html.required.set(true)
        html.outputLocation.set(file("$rootDir/detekt/reports/feature-ondemand-mediator.html"))
    }
}
