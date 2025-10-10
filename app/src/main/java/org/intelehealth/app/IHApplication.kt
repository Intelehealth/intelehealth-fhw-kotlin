package org.intelehealth.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import com.github.ajalt.timberkt.Timber
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import org.intelehealth.app.ui.user.observer.AppUsesStatisticsObserver
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 19-12-2024 - 12:32.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * The main application class for the IH application.
 *
 * This class extends [Application] and serves as the entry point for the application.
 * It's responsible for initializing core components and services that are needed
 * throughout the app's lifecycle.
 *
 * Key responsibilities include:
 * - **Logging:** Setting up [Timber] for logging during development.
 * - **Firebase:** Initializing [FirebaseApp] for Firebase services.
 * - **Crash Reporting:** Configuring [FirebaseCrashlytics] for crash reporting (disabled by default).
 * - **Dependency Injection:** Enabling Hilt for dependency injection with the `@HiltAndroidApp` annotation.
 * - **Background Tasks:** Configuring [WorkManager] with [HiltWorkerFactory] for managing background tasks.
 *
 * This class is annotated with `@HiltAndroidApp`, which triggers Hilt's code generation
 * and sets up the application-level dependency injection container.
 */
@HiltAndroidApp
class IHApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var appUsesStatisticsObserver: AppUsesStatisticsObserver

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     *
     * This method performs the following initializations:
     * - **Timber:** Plants a [Timber.DebugTree] for logging in debug builds.
     * - **Firebase:** Initializes [FirebaseApp] for access to Firebase services.
     * - **FirebaseCrashlytics:** Disables automatic crash reporting by setting
     *   `isCrashlyticsCollectionEnabled` to `false`.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FirebaseApp.initializeApp(this)

        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = BuildConfig.ACTIVE_CRASH
        ProcessLifecycleOwner.get().lifecycle.addObserver(appUsesStatisticsObserver)
    }

    /**
     * Provides the configuration for WorkManager.
     *
     * This configuration sets the [HiltWorkerFactory] to enable Hilt-injected workers,
     * allowing background tasks to use dependencies provided by Hilt.
     */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

}
