package org.intelehealth.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.intelehealth.common.utility.DateTimeResource

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

    @Provides
    fun provideDateResource(
        @ApplicationContext context: Context
    ): DateTimeResource? {
        DateTimeResource.build(context)
        return DateTimeResource.getInstance()
    }
}