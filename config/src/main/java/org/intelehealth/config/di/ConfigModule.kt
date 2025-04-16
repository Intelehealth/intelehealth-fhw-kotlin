package org.intelehealth.config.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import org.intelehealth.config.BuildConfig
import org.intelehealth.config.network.ConfigRestClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Vaghela Mithun R. on 19-12-2024 - 18:07.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {
    @Provides
    @Singleton
    fun provideConfigRestClient(builder: Retrofit.Builder): ConfigRestClient {
        return builder.baseUrl("${BuildConfig.SERVER_URL}:4004").build().create(ConfigRestClient::class.java)
    }

    @Provides
    @Singleton
    fun provideCoroutineDispatcher() = Dispatchers.IO
}