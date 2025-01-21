package org.intelehealth.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.intelehealth.data.network.BuildConfig
import org.intelehealth.data.network.RestClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Vaghela Mithun R. on 20-12-2024 - 19:02.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideConfigRestClient(builder: Retrofit.Builder): RestClient {
        return builder.baseUrl(BuildConfig.SERVER_URL).build().create(RestClient::class.java)
    }
}