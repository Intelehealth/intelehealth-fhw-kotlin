package extensions

import Deps
import Modules
import TestDeps
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Configures the dependencies for the application module.
 *
 * This function adds all the necessary dependencies to the app module,
 * including project modules, external libraries, and AndroidX components.  It
 * is intended to be used within the `dependencies` block of the app module's
 * `build.gradle.kts` file.
 *
 * The dependencies are categorized as follows:
 * - **Project Modules:** References to other modules within the project,
 *   providing modularity and separation of concerns.
 * - **External Libraries:** Third-party libraries for specific functionalities
 *   like HTML parsing (Jsoup), biometric authentication, country code picker,
 *   image loading (Glide), and more.
 * - **AndroidX Components:** Jetpack libraries for UI, navigation, lifecycle
 *   management, data persistence (Room), and other core Android functionalities.
 * - **Dependency Injection:** Hilt libraries for managing dependencies and
 *   performing dependency injection.
 *
 * The function uses Gradle's [DependencyHandler] to add these dependencies with
 * appropriate configurations (e.g., `implementation`, `kapt`).
 */
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

/**
 * Configures the dependencies for the Firebase Cloud Messaging (FCM) module.
 *
 * This function adds the necessary dependencies to enable Firebase integration
 * within the FCM module of the application. It includes dependencies for core
 * FCM functionality, as well as other Firebase services like Crashlytics,
 * Analytics, Realtime Database, and Remote Config.  It also includes a
 * dependency on the application's `common` module and the AndroidX Lifecycle
 * Process library.
 *
 * This function is intended to be used within the `dependencies` block of the
 * FCM module's `build.gradle.kts` file.
 *
 * The dependencies are added using the `api` configuration, making them
 * transitively available to other modules that depend on the FCM module.
 */
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

/**
 * Configures the dependencies for the "favorite" feature module.
 *
 * This function adds the necessary dependencies to the module responsible for
 * managing user favorites within the application. It includes dependencies on
 * other project modules (`common`, `app`, `data`), the Navigation component for
 * UI navigation, and Hilt for dependency injection.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "favorite" module's `build.gradle.kts` file.
 */
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

/**
 * Configures the dependencies for the "on-demand" feature modules.
 *
 * This function adds the necessary dependencies to the "on-demand" feature
 * modules, which are loaded dynamically as needed. It includes dependencies on
 * other project modules (`common`, `app`, `provider`, `installer`, `mediator`),
 * as well as libraries for UI components, WorkManager, Room, and Hilt for
 * dependency injection.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "on-demand" feature modules' `build.gradle.kts` files.
 */
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

/**
 * Configures the dependencies for the "on-demand video" feature module.
 *
 * This function adds the necessary dependencies to the module responsible for
 * providing on-demand video functionality within the application. It includes
 *dependencies on various project modules for core features and data access, as
 * well as external libraries for UI, networking, data persistence, video
 * processing, and dependency injection.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "on-demand video" module's `build.gradle.kts` file.
 *
 * The dependencies include:
 * - **Project Modules:** Dependencies on core modules like `common`, `app`,
 *   `provider`, `installer`, `mediator`, and `fcm` for shared functionality,
 *   application context, data provision, installation management, mediation,
 *   and Firebase Cloud Messaging.
 * - **AndroidX Components:** Libraries for lifecycle management
 *   (`viewModel`, `liveData`, `runtime`), UI (`material`, `appcompat`,
 *   `constraintLayout`), data persistence (`Room`), and background tasks
 *   (`WorkManager`).
 * - **External Libraries:** Libraries for networking (`Volley`), image loading
 *   (`Glide`), real-time video communication (`LiveKit`), camera access
 *   (`CameraX`), UI effects (`RippleBackground`), and potentially data
 *   serialization (Protobuf, commented out).
 * - **Dependency Injection:** Hilt libraries for managing dependencies and
 *   performing dependency injection, including support for workers.
 */
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

/**
 * Configures the dependencies for the "common" module.
 *
 * This function adds the necessary dependencies to the common module, which
 * typically contains shared code, utilities, and resources used by other
 * modules in the application. It includes dependencies for UI components,
 * core AndroidX libraries, Kotlin extensions (KTX), coroutines, networking
 * (Retrofit, OkHttp), logging (Timber), JSON handling (Gson), real-time
 * communication (Socket.IO), dependency injection (Hilt), background tasks
 * (WorkManager), and potentially JavaScript execution (Rhino).
 *
 * This function is intended to be used within the `dependencies` block of the
 * "common" module's `build.gradle.kts` file.
 *
 * The dependencies are added using a combination of `api` and `implementation`
 * configurations:
 * - `api`: Dependencies with the `api` configuration are exposed to other
 *   modules that depend on the "common" module. This means that if a module
 *   depends on "common", it will also have access to the dependencies declared
 *   with `api` in this function.
 * - `implementation`: Dependencies with the `implementation` configuration are
 *   internal to the "common" module and are not directly exposed to other
 *   modules.
 */
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

/**
 * Configures the dependencies for the "resource" module.
 *
 * This function adds the necessary dependencies to the resource module, which
 * typically contains UI-related resources like layouts, drawables, styles,
 * and potentially custom views or components. It includes dependencies for
 * core Android UI components from Material Design and AndroidX.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "resource" module's `build.gradle.kts` file.
 */
fun DependencyHandler.resourceModuleDeps() {
    // AndroidX Libs
    implementation(Deps.Android.material)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.Constraint.constraintLayout)
}

/**
 * Configures the dependencies for the "config" module.
 *
 * This function adds the necessary dependencies to the config module, which is
 * likely responsible for managing application configuration data, settings,
 * and potentially feature flags. It includes dependencies on other project
 * modules (`core`, `common`), AndroidX lifecycle components, coroutines for
 * asynchronous operations, Room for data persistence, WorkManager for
 * background tasks, and Hilt for dependency injection.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "config" module's `build.gradle.kts` file.
 */
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

/**
 * Configures the dependencies for the "offline" module.
 *
 * This function adds the necessary dependencies to the offline module, which
 * is likely responsible for handling offline data storage, retrieval, and
 * synchronization within the application. It includes dependencies on other
 * project modules (`core`, `common`), AndroidX lifecycle components (excluding
 * ViewModel, possibly intentionally), coroutines for asynchronous operations,
 * Room for data persistence, Gson for JSON handling, and Hilt for dependency
 * injection.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "offline" module's `build.gradle.kts` file.
 */
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

/**
 * Configures the dependencies for the "core" module.
 *
 * This function adds the necessary dependencies to the core module, which
 * likely provides fundamental functionalities and data handling for the
 * application. It includes dependencies on the `common` module for shared
 * resources and utilities, networking libraries (Retrofit, OkHttp) for making
 * API calls, Gson for JSON serialization/deserialization, and Hilt for
 * dependency injection.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "core" module's `build.gradle.kts` file.
 */
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

/**
 * Configures the dependencies for the "feature installer" module.
 *
 * This function adds the necessary dependencies to the feature installer
 * module, which is likely responsible for managing the installation of
 * optional or on-demand features in the application using the Play Feature
 * Delivery API. It includes dependencies on the `common` module for shared
 * resources, UI components from Material Design and AndroidX, the Play Feature
 * Delivery and In-App Review libraries, and Hilt for dependency injection.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "feature installer" module's `build.gradle.kts` file.
 */
fun DependencyHandler.featureInstallerModuleDeps() {
    implementation(project(Modules.common))

    // AndroidX Libs
    implementation(Deps.Android.material)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.Constraint.constraintLayout)

    // Play core libraries
    implementation(Deps.Play.featureDelivery)
    implementation(Deps.Play.review)

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

/**
 * Configures the dependencies for the "feature mediator" module.
 *
 * This function adds the necessary dependencies to the feature mediator
 * module, which is likely responsible for managing the interaction between
 * different feature modules in the application. It includes dependencies on
 * the `common` module for shared resources and Hilt for dependency injection.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "feature mediator" module's `build.gradle.kts` file.
 */
fun DependencyHandler.featureOnDemandMediatorModuleDeps() {
    implementation(project(Modules.common))

    // Hilt
    implementation(Deps.Dagger.hiltAndroid)
    kapt(Deps.Dagger.hiltAndroidCompiler)
}

/**
 * Configures the dependencies for the "network" module.
 *
 * This function adds the necessary dependencies to the network module, which
 * is likely responsible for handling network communication and API calls
 * within the application. It includes dependencies on other project modules
 * (`core`, `offline`, `common`), Retrofit and OkHttp for networking, Gson for
 * JSON handling, WorkManager for background tasks, and Hilt for dependency
 * injection.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "network" module's `build.gradle.kts` file.
 */
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

/**
 * Configures the dependencies for the "provider" module.
 *
 * This function adds the necessary dependencies to the provider module,
 * which is likely responsible for providing data and services to other
 * modules in the application. It includes dependencies on other project
 * modules (`network`, `offline`, `common`), WorkManager for background tasks,
 * Room for data persistence, Gson for JSON handling, and Hilt for dependency
 * injection.
 *
 * This function is intended to be used within the `dependencies` block of the
 * "provider" module's `build.gradle.kts` file.
 */
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

/**
 * Configures the dependencies for unit tests.
 *
 * This function adds the necessary dependencies for writing and running unit
 * tests within the module. It includes the JUnit Platform for test execution
 * and provides commented-out dependencies for other common testing libraries
 * such as AndroidX Test Core, Coroutines Test, MockWebServer, MockK, and Truth,
 * which can be enabled as needed.
 *
 * This function is intended to be used within the `dependencies` block of a
 * module's `build.gradle.kts` file.  Dependencies are added with the
 * `testImplementation` configuration, ensuring they are only included in the
 * test build variant.
 */
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

/**
 * Configures the dependencies for Android instrumentation tests.
 *
 * This function adds the necessary dependencies for writing and running
 * instrumentation tests, which are tests that run on an Android device or
 * emulator. It includes dependencies for AndroidX JUnit integration, Espresso
 * for UI testing, and provides commented-out dependencies for other common
 * testing libraries such as AndroidX Core Testing, Navigation Testing,
 * Coroutines Test, MockWebServer, MockK, and Truth, which can be enabled as
 * needed.
 *
 * This function is intended to be used within the `dependencies` block of a
 * module's `build.gradle.kts` file. Dependencies are added with the
 * `androidTestImplementation` configuration, ensuring they are only included
 * in the Android instrumentation test build variant.
 */
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
/**
 * Adds a dependency to the `implementation` configuration.
 *
 * This extension function mimics the behavior of the `implementation` keyword
 * in Gradle Kotlin DSL files. It allows you to add dependencies to the
 * `implementation` configuration of a module in a way that closely resembles
 * the standard Gradle Kotlin DSL syntax.
 *
 * The `implementation` configuration is used for dependencies that are
 * required for the internal implementation of a module and are not exposed to
 * other modules that depend on it.
 *
 * @param dependencyNotation The dependency to be added, specified using a
 *   Gradle dependency notation (e.g., a string like "com.example:library:1.0.0"
 *   or a [Dependency] object).
 * @return The [Dependency] object that was added, or `null` if the dependency
 *   could not be added.
 */
@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

/**
 * Adds a dependency to the `api` configuration.
 *
 * This extension function allows you to add dependencies to the `api`
 * configuration of a module.  Dependencies in the `api` configuration are
 * exposed to other modules that depend on this module.  This means that if
 * module A depends on module B, and module B has a dependency on library C in
 * its `api` configuration, then module A will also have access to library C.
 *
 * Use the `api` configuration for dependencies that form part of the public
 * API of your module and should be accessible to other modules that use it.
 *
 * @param dependencyNotation The dependency to be added, specified using a
 *   Gradle dependency notation (e.g., a string like "com.example:library:1.0.0"
 *   or a [Dependency] object).
 * @return The [Dependency] object that was added, or `null` if the dependency
 *   could not be added.
 */
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
