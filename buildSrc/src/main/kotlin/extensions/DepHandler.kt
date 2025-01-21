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
    api(project(Modules.installer))
    api(project(Modules.mediator))
    implementation(project(Modules.config))
    implementation(project(Modules.provider))
    api(project(Modules.fcm))

    // Glide
    implementation(Deps.Glide.glide)
    // Navigation Component
    implementation(Deps.AndroidX.Navigation.ui)
    implementation(Deps.AndroidX.Navigation.fragment)
//    implementation(Deps.AndroidX.Navigation.commonKtx)
//    implementation(Deps.AndroidX.Navigation.navigationSafeArguments)
//    implementation(Deps.AndroidX.Navigation.dynamicFeaturesFragment)

//    api(Deps.Android.material)
//    api(Deps.AndroidX.appcompat)
//    api(Deps.AndroidX.Constraint.constraintLayout)
//    api(Deps.AndroidX.Fragment.fragmentKtx)
//    api(Deps.AndroidX.Activity.activityKtx)

//    implementation(Deps.AndroidX.Lifecycle.viewModel)
//    implementation(Deps.AndroidX.Lifecycle.liveData)
//    implementation(Deps.AndroidX.Lifecycle.runtime)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltKapt)
}

/*
* Adds required dependencies to home module
* */
fun DependencyHandler.fcmModuleDeps() {

    api(project(Modules.common))

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

    // Work Manager
    api(Deps.AndroidX.Work.runtimeKtx)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltKapt)
}

/*
* Add required dependencies to video module
* */
fun DependencyHandler.featureOnDemandVideoModuleDeps() {
    // Libraries
    implementation(project(Modules.common))
    implementation(project(Modules.app))
    implementation(project(Modules.provider))

    implementation(Deps.AndroidX.Lifecycle.viewModel)
    implementation(Deps.AndroidX.Lifecycle.liveData)
    implementation(Deps.AndroidX.Lifecycle.runtime)

//    implementation(Deps.Protobuf.javalite)
    implementation(Deps.LiveKit.android)
    implementation(Deps.LiveKit.cameraX)
    implementation(Deps.Glide.glide)
    implementation(Deps.Volley.volley)
    implementation(Deps.RippleBackground.rippleBackground)

    // Work Manager
    api(Deps.AndroidX.Work.runtimeKtx)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltKapt)
}

/*
* Add required dependencies to common module
* */
fun DependencyHandler.commonModuleDeps() {
    // Module
    api(project(Modules.resource))

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


    // Timber
    api(Deps.Timber.timber)
    // Gson
    api(Deps.Gson.gson)

    api(Deps.IOSocket.ioSocket)

    // Hilt
    api(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

/*
* Add required dependencies to common module
* */
fun DependencyHandler.resourceModuleDeps() {
    // AndroidX Libs
    api(Deps.Android.material)
    api(Deps.AndroidX.appcompat)
    api(Deps.AndroidX.Constraint.constraintLayout)
}

/*
* Add required dependencies to common module
* */
fun DependencyHandler.configModuleDeps() {
    // Module
    api(project(Modules.core))

    api(Deps.AndroidX.Lifecycle.viewModel)
    api(Deps.AndroidX.Lifecycle.liveData)
    api(Deps.AndroidX.Lifecycle.runtime)

    // Coroutines
//    api(Deps.Coroutines.core)
//    api(Deps.Coroutines.android)

    // Room
    api(Deps.AndroidX.Room.runtime)
    api(Deps.AndroidX.Room.ktx)
    kapt(Deps.AndroidX.Room.compiler)

    // Work Manager
    api(Deps.AndroidX.Work.runtimeKtx)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltKapt)
}

/*
* Add required dependencies to common module
* */
fun DependencyHandler.offlineModuleDeps() {
    // Module
    api(project(Modules.core))

//    api(Deps.AndroidX.Lifecycle.viewModel)
    api(Deps.AndroidX.Lifecycle.liveData)
    api(Deps.AndroidX.Lifecycle.runtime)

    // Coroutines
//    api(Deps.Coroutines.core)
//    api(Deps.Coroutines.android)

    // Room
    api(Deps.AndroidX.Room.runtime)
    api(Deps.AndroidX.Room.ktx)
    kapt(Deps.AndroidX.Room.compiler)

//    // Work Manager
//    api(Deps.AndroidX.Work.runtimeKtx)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
//    implementation(Deps.Dagger.hiltWorker)
//    kapt(Deps.Dagger.hiltKapt)
}

/*
* Add required dependencies to data module
* */
fun DependencyHandler.coreModuleDeps() {
    api(project(Modules.common))

    // Retrofit
//    api(Deps.Retrofit.retrofit)
//    api(Deps.Retrofit.gsonConverter)
//
    //OKHttp
//    api(Deps.OkHttp.okhttp)
    api(Deps.OkHttp.logging)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

fun DependencyHandler.featureInstallerModuleDeps() {
    api(project(Modules.common))

    // Retrofit
    api(Deps.Play.featureDelivery)
    api(Deps.Play.review)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

fun DependencyHandler.featureOnDemandMediatorModuleDeps() {
    api(project(Modules.common))

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

/*
* Add required dependencies to data module
* */
fun DependencyHandler.dataModuleDeps() {
    implementation(project(Modules.common))
}

/*
* Add required dependencies to domain module
* */
fun DependencyHandler.networkModuleDeps() {
    api(project(Modules.core))
    api(project(Modules.offline))
    // Work Manager
    api(Deps.AndroidX.Work.runtimeKtx)
    // Hilt
    api(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    api(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltKapt)
}

/*
* Add required dependencies to domain module
* */
fun DependencyHandler.providerModuleDeps() {
    api(project(Modules.network))
    // Work Manager
    api(Deps.AndroidX.Work.runtimeKtx)
    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
    implementation(Deps.Dagger.hiltWorker)
    kapt(Deps.Dagger.hiltKapt)
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

private fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

private fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? =
    add("debugImplementation", dependencyNotation)

private fun DependencyHandler.testRuntimeOnly(dependencyNotation: Any): Dependency? =
    add("testRuntimeOnly", dependencyNotation)

private fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

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
