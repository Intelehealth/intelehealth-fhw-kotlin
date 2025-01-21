package org.intelehealth.app.di.module

import android.content.Context
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
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseApp(@ApplicationContext context: Context) = FirebaseApp.initializeApp(context)

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideAssetManager(@ApplicationContext context: Context) = context.assets
}