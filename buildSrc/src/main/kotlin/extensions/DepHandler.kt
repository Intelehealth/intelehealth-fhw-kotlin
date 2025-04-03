package extensions

import Deps
import Modules
import TestDeps
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/*
* Adds required dependencies to app module
* */
fun DependencyHandler.appModuleDeps() {
    // Libraries
    implementation(project(Modules.installer))
    implementation(project(Modules.mediator))
    implementation(project(Modules.config))
    implementation(project(Modules.provider))
    implementation(project(Modules.network))
    implementation(project(Modules.offline))
    implementation(project(Modules.fcm))
    implementation(project(Modules.common))

    //jsoup to parse html
    implementation(Deps.Jsoup.jsoup)

    //fingerprint lock
    implementation(Deps.AndroidX.Biometric.biometric)

    //CCP Library
    implementation(Deps.CCPLibrary.ccp)

    // Glide
    implementation(Deps.Glide.glide)
    // Navigation Component
    implementation(Deps.AndroidX.Navigation.ui)
    implementation(Deps.AndroidX.Navigation.fragment)

    implementation(Deps.Android.material)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.Constraint.constraintLayout)
    implementation(Deps.AndroidX.Fragment.fragmentKtx)
    implementation(Deps.AndroidX.Activity.activityKtx)

    implementation(Deps.AndroidX.Lifecycle.viewModel)
    implementation(Deps.AndroidX.Lifecycle.liveData)
    implementation(Deps.AndroidX.Lifecycle.runtime)

    implementation(Deps.AndroidX.Room.runtime)
    implementation(Deps.AndroidX.Room.ktx)
    kapt(Deps.AndroidX.Room.compiler)

//    detektPlugins(Deps.Detekt.formatting)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltWorkerKapt)
}

/*
* Adds required dependencies to home module
* */
fun DependencyHandler.fcmModuleDeps() {

    implementation(project(Modules.common))

    // Libraries
    api(platform(Deps.Firebase.bom))
    api(Deps.Firebase.messaging)
    api(Deps.Firebase.crashlytics)
    api(Deps.Firebase.analytics)
    api(Deps.Firebase.database)
    api(Deps.Firebase.config)

    api(Deps.AndroidX.Lifecycle.process)
}

/*
* Add required dependencies to favorite module
* */
fun DependencyHandler.favoriteModuleDeps() {
    // Libraries
    implementation(project(Modules.common))
    implementation(project(Modules.app))
    implementation(project(Modules.data))

    // Navigation components
    implementation(Deps.AndroidX.Navigation.fragment)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

/*
* Add required dependencies to chat module
* */
fun DependencyHandler.featureOnDemandChatModuleDeps() {
    // Libraries
    implementation(project(Modules.common))
    implementation(project(Modules.app))
    implementation(project(Modules.provider))
    implementation(project(Modules.installer))
    implementation(project(Modules.mediator))

    implementation(Deps.Android.material)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.Constraint.constraintLayout)
    // Work Manager
    implementation(Deps.AndroidX.Work.runtimeKtx)

    implementation(Deps.AndroidX.Room.runtime)
    implementation(Deps.AndroidX.Room.ktx)
    kapt(Deps.AndroidX.Room.compiler)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltWorkerKapt)
}

/*
* Add required dependencies to video module
* */
fun DependencyHandler.featureOnDemandVideoModuleDeps() {
    // Libraries
    implementation(project(Modules.common))
    implementation(project(Modules.app))
    implementation(project(Modules.provider))
    implementation(project(Modules.installer))
    implementation(project(Modules.mediator))
    implementation(project(Modules.fcm))

    implementation(Deps.AndroidX.Lifecycle.viewModel)
    implementation(Deps.AndroidX.Lifecycle.liveData)
    implementation(Deps.AndroidX.Lifecycle.runtime)

    implementation(Deps.Android.material)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.Constraint.constraintLayout)

    implementation(Deps.AndroidX.Room.runtime)
    implementation(Deps.AndroidX.Room.ktx)
    kapt(Deps.AndroidX.Room.compiler)

//    implementation(Deps.Protobuf.javalite)
    implementation(Deps.LiveKit.android)
    implementation(Deps.LiveKit.cameraX)
    implementation(Deps.Glide.glide)
    implementation(Deps.Volley.volley)
    implementation(Deps.RippleBackground.rippleBackground)

    // Work Manager
    implementation(Deps.AndroidX.Work.runtimeKtx)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltWorkerKapt)
}

/*
* Add required dependencies to common module
* */
fun DependencyHandler.commonModuleDeps() {
    // Module
    api(project(Modules.resource))

    // AndroidX Libs
    implementation(Deps.Android.material)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.Constraint.constraintLayout)

    implementation(Deps.Rhino.android)

    // KTX
    api(Deps.AndroidX.coreKtx)
    api(Deps.AndroidX.Fragment.fragmentKtx)
    api(Deps.AndroidX.Activity.activityKtx)
    api(Deps.AndroidX.Preference.preferenceKtx)

    // Coroutines
    api(Deps.Coroutines.core)
    api(Deps.Coroutines.android)

    // Retrofit
    api(Deps.Retrofit.retrofit)
    api(Deps.Retrofit.gsonConverter)

    //OKHttp
    api(Deps.OkHttp.okhttp)
    api(Deps.OkHttp.logging)

    // Timber
    api(Deps.Timber.timber)
    // Gson
    api(Deps.Gson.gson)

    api(Deps.IOSocket.ioSocket)

    // Hilt
    api(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)

    // Work Manager
    api(Deps.AndroidX.Work.runtimeKtx)

    // Hilt
    api(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    api(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltWorkerKapt)
}

/*
* Add required dependencies to common module
* */
fun DependencyHandler.resourceModuleDeps() {
    // AndroidX Libs
    implementation(Deps.Android.material)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.Constraint.constraintLayout)
}

/*
* Add required dependencies to common module
* */
fun DependencyHandler.configModuleDeps() {
    // Module
    implementation(project(Modules.core))
    implementation(project(Modules.common))

    implementation(Deps.AndroidX.Lifecycle.viewModel)
    implementation(Deps.AndroidX.Lifecycle.liveData)
    implementation(Deps.AndroidX.Lifecycle.runtime)

    // Coroutines
    implementation(Deps.Coroutines.core)
    implementation(Deps.Coroutines.android)

    // Room
    implementation(Deps.AndroidX.Room.runtime)
    implementation(Deps.AndroidX.Room.ktx)
    kapt(Deps.AndroidX.Room.compiler)

    // Work Manager
    implementation(Deps.AndroidX.Work.runtimeKtx)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltWorkerKapt)
}

/*
* Add required dependencies to common module
* */
fun DependencyHandler.offlineModuleDeps() {
    // Module
    implementation(project(Modules.core))
    implementation(project(Modules.common))

//    api(Deps.AndroidX.Lifecycle.viewModel)
    implementation(Deps.AndroidX.Lifecycle.liveData)
    implementation(Deps.AndroidX.Lifecycle.runtime)

//     Coroutines
    implementation(Deps.Coroutines.core)
    implementation(Deps.Coroutines.android)

    // Room
    implementation(Deps.AndroidX.Room.runtime)
    implementation(Deps.AndroidX.Room.ktx)
    kapt(Deps.AndroidX.Room.compiler)

    implementation(Deps.Gson.gson)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

/*
* Add required dependencies to data module
* */
fun DependencyHandler.coreModuleDeps() {
    implementation(project(Modules.common))

    // Retrofit
    implementation(Deps.Retrofit.retrofit)
    implementation(Deps.Retrofit.gsonConverter)
//
    //OKHttp
    implementation(Deps.OkHttp.okhttp)
    implementation(Deps.OkHttp.logging)

    // Gson
    implementation(Deps.Gson.gson)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

fun DependencyHandler.featureInstallerModuleDeps() {
    implementation(project(Modules.common))

    // AndroidX Libs
    implementation(Deps.Android.material)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.Constraint.constraintLayout)

    // Retrofit
    implementation(Deps.Play.featureDelivery)
    implementation(Deps.Play.review)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

fun DependencyHandler.featureOnDemandMediatorModuleDeps() {
    implementation(project(Modules.common))

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

/*
* Add required dependencies to data module
* */
//fun DependencyHandler.dataModuleDeps() {
//    implementation(project(Modules.common))
//}

/*
* Add required dependencies to domain module
* */
fun DependencyHandler.networkModuleDeps() {
    implementation(project(Modules.core))
    implementation(project(Modules.offline))
    implementation(project(Modules.common))

    // Retrofit
    implementation(Deps.Retrofit.retrofit)
    implementation(Deps.Retrofit.gsonConverter)
//
    //OKHttp
    implementation(Deps.OkHttp.okhttp)
    implementation(Deps.OkHttp.logging)

    // Gson
    implementation(Deps.Gson.gson)

    // Work Manager
    implementation(Deps.AndroidX.Work.runtimeKtx)
    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltWorkerKapt)
}

/*
* Add required dependencies to domain module
* */
fun DependencyHandler.providerModuleDeps() {
    implementation(project(Modules.network))
    implementation(project(Modules.offline))
    implementation(project(Modules.common))
    // Work Manager
    implementation(Deps.AndroidX.Work.runtimeKtx)

    // Room
    implementation(Deps.AndroidX.Room.runtime)
    implementation(Deps.AndroidX.Room.ktx)
    kapt(Deps.AndroidX.Room.compiler)

    implementation(Deps.Gson.gson)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltWorkerKapt)
}

/*
* Add Unit test dependencies
* */
fun DependencyHandler.unitTestDeps() {
    // (Required) writing and executing Unit Tests on the JUnit Platform
    testImplementation(TestDeps.JUnit.junit)

//    // AndroidX Test - JVM testing
//    testImplementation(TestDeps.AndroidX.coreKtx)
//
//    // Coroutines Test
//    testImplementation(TestDeps.Coroutines.coroutines)
//
//    // MockWebServer
//    testImplementation(TestDeps.MockWebServer.mockwebserver)
//
//    // MocKK
//    testImplementation(TestDeps.MockK.mockK)
//
//    // Truth
//    testImplementation(TestDeps.truth)
}

/*
* Add Instrumentation test dependencies
* */
fun DependencyHandler.androidTestDeps() {
    // AndroidX Test - Instrumented testing
    androidTestImplementation(TestDeps.AndroidX.androidX_jUnit)
//    androidTestImplementation(TestDeps.AndroidX.coreTesting)

    // Espresso
    androidTestImplementation(TestDeps.espressoCore)

    // Navigation Testing
//    androidTestImplementation(TestDeps.AndroidX.navigationTest)
//
//    // Coroutines Test
//    androidTestImplementation(TestDeps.Coroutines.coroutines)
//
//    // MockWebServer
//    androidTestImplementation(TestDeps.MockWebServer.mockwebserver)
//
//    // MockK
//    androidTestImplementation(TestDeps.MockK.mockK)
//
//    // Truth
//    androidTestImplementation(TestDeps.truth)
}


/*
 * These extensions mimic the extensions that are generated on the fly by Gradle.
 * They are used here to provide above dependency syntax that mimics Gradle Kotlin DSL
 * syntax in module\build.gradle.kts files.
 */
@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.api(dependencyNotation: Any): Dependency? = add("api", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.kapt(dependencyNotation: Any): Dependency? = add("kapt", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.detektPlugins(dependencyNotation: Any): Dependency? =
    add("detektPlugins", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? =
    add("debugImplementation", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.testRuntimeOnly(dependencyNotation: Any): Dependency? =
    add("testRuntimeOnly", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.project(
    path: String, configuration: String? = null
): ProjectDependency {
    val notation = if (configuration != null) {
        mapOf("path" to path, "configuration" to configuration)
    } else {
        mapOf("path" to path)
    }

    return uncheckedCast(project(notation))
}

@Suppress("unchecked_cast", "nothing_to_inline", "detekt.UnsafeCast")
private inline fun <T> uncheckedCast(obj: Any?): T = obj as T
