import extensions.androidTestDeps
import extensions.featureOnDemandVideoModuleDeps
import extensions.unitTestDeps

plugins {
    alias(libs.plugins.androidx.dynamic.feature)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin(Plugins.KAPT)
    id(Plugins.PARCELIZE)
    id(Plugins.DAGGER_HILT)
}

apply("${rootProject.projectDir}/whitelabel.gradle")

android {
    namespace = "org.intelehealth.app.feature.video"
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
    featureOnDemandVideoModuleDeps()
    unitTestDeps()
    androidTestDeps()
//    implementation(project(":core"))
//    implementation(project(":core:socket"))
//    implementation("androidx.core:core-ktx:1.13.1")
//    implementation(project(":app"))
//    implementation(project(":features:ondemand:mediator"))
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.2.1")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
//
//    api("androidx.core:core-ktx:1.13.1")
//    api("androidx.appcompat:appcompat:1.7.0")
//    api("com.google.android.material:material:1.12.0")
//    api("androidx.activity:activity-ktx:1.9.3")
//    api("androidx.fragment:fragment-ktx:1.8.4")
//    // WebRtc
//    api("io.livekit:livekit-android:1.5.2")
//    api("com.google.protobuf:protobuf-javalite:3.23.3")
//
//    // Lifecycle
//    api("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
//    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
//    api("androidx.lifecycle:lifecycle-common-java8:2.8.6")
//    api("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
//
//    // Preference
//    api("androidx.preference:preference-ktx:1.2.1")
//
//    // Coroutines for execute concurrent tasks
////    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
////    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
//
//    // Timber logging
//    api("com.github.ajalt:timberkt:1.5.1")
//
//    // debugImplementation because LeakCanary should only run in debug builds.
////    debugapi("com.squareup.leakcanary:leakcanary-android:2.8.1'
//
//    api("com.amazonaws:aws-android-sdk-s3:2.17.1")
//
//    api("com.skyfishjy.ripplebackground:library:1.0.1")
//
//    api("io.socket:socket.io-client:1.0.0") {
//        exclude(
//            group = "org.json", module = "json"
//        )
//    }
//
//    api("com.android.volley:volley:1.2.1")
//    api("com.github.bumptech.glide:glide:4.14.2")
//    api("com.google.code.gson:gson:2.10.1")
//
//    val room_version = "2.6.1"
//    api("androidx.room:room-ktx:$room_version")
//    api("androidx.room:room-runtime:$room_version")
////    annotationProcessor "androidx.room:room-compiler:$room_version"
//    // To use Kotlin annotation processing tool (kapt)
//    kapt("androidx.room:room-compiler:$room_version")
//
//    //Retrofit
//    api("com.squareup.retrofit2:retrofit:2.9.0")
//    api("com.squareup.retrofit2:converter-gson:2.9.0")
//    api("com.squareup.okhttp3:logging-interceptor:4.10.0")
//
//    val hiltVersion = "2.49"
//    api("com.google.dagger:hilt-android:$hiltVersion")
//    annotationProcessor("com.google.dagger:hilt-compiler:$hiltVersion")
//    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
//
//    // WorkManager
//    val work_version = "2.9.1"
//    api("androidx.work:work-runtime-ktx:$work_version")
//    api("androidx.hilt:hilt-work:1.2.0")
//    // When using Kotlin.
//    kapt("androidx.hilt:hilt-compiler:1.2.0")

//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
}