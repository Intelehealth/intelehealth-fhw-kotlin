package org.intelehealth.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Created by Vaghela Mithun R. on 18-12-2024 - 18:16.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    // Add your core module dependencies here
    // e.g. @Provides fun provideSomeCoreDependency(): SomeCoreDependency = SomeCoreDependencyImpl()
}