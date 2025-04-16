package org.intelehealth.app.di.module

import android.content.Context
import android.content.res.AssetManager
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.intelehealth.data.offline.di.OfflineDatabaseModule
import javax.inject.Singleton

/**
 * Created by Vaghela Mithun R. on 01-01-2025 - 20:23.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * Dagger Hilt module for providing application-scoped dependencies.
 *
 * This module is installed in the [SingletonComponent], meaning that the dependencies
 * provided here will have a single instance throughout the application's lifecycle.
 * It provides dependencies such as:
 * - [FirebaseApp]: The initialized Firebase application instance.
 * - [WorkManager]: The WorkManager instance for managing background tasks.
 * - [AssetManager]: The AssetManager for accessing application assets.
 *
 * These dependencies are commonly used across different parts of the application and
 * are therefore provided as singletons.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the initialized [FirebaseApp] instance.
     *
     * @param context The application context.
     * @return The initialized [FirebaseApp] instance.
     */
    @Provides
    @Singleton
    fun provideFirebaseApp(@ApplicationContext context: Context) = FirebaseApp.initializeApp(context)

    /**
     * Provides the [WorkManager] instance.
     *
     * @param context The application context.
     * @return The [WorkManager] instance.
     */
    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager = WorkManager.getInstance(context)

    /**
     * Provides the [AssetManager] instance.
     *
     * @param context The application context.
     * @return The [AssetManager] instance.
     */
    @Provides
    @Singleton
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager = context.assets
}