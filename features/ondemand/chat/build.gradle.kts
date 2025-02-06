import extensions.androidTestDeps
import extensions.featureOnDemandChatModuleDeps
import extensions.unitTestDeps

//plugins {
//    id("com.android.dynamic-feature")
//    id("org.jetbrains.kotlin.android")
//    id("kotlin-parcelize")
//    id("kotlin-kapt")
//    id("dagger.hilt.android.plugin")
//}

plugins {
    alias(libs.plugins.androidx.dynamic.feature)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.detekt.plugin)
}

apply("${rootProject.projectDir}/whitelabel.gradle")

android {
    namespace = "org.intelehealth.feature.chat"
    compileSdk = AndroidConfig.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidConfig.MIN_SDK

        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER
//        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    featureOnDemandChatModuleDeps()
    unitTestDeps()
    androidTestDeps()
//    implementation(project(":app"))
//    implementation("androidx.core:core-ktx:1.13.1")
//    implementation("com.google.code.gson:gson:2.10.1")
//
//    val room_version = "2.6.1"
//    implementation("androidx.room:room-ktx:$room_version")
//    implementation("androidx.room:room-runtime:$room_version")
////    annotationProcessor "androidx.room:room-compiler:$room_version"
//    // To use Kotlin annotation processing tool (kapt)
//    kapt("androidx.room:room-compiler:$room_version")
//
//    //Retrofit
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
//
//    val hiltVersion = "2.49"
//    implementation("com.google.dagger:hilt-android:$hiltVersion")
////    annotationProcessor("com.google.dagger:hilt-compiler:$hiltVersion")
//    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
//
//    // WorkManager
//    val work_version = "2.9.1"
//    implementation("androidx.work:work-runtime-ktx:$work_version")
//    implementation("androidx.hilt:hilt-work:1.2.0")
//    // When using Kotlin.
//    kapt("androidx.hilt:hilt-compiler:1.2.0")

//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.2.1")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
