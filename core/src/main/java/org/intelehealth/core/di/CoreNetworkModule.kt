package org.intelehealth.core.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.intelehealth.common.utility.CommonConstants.HTTP_REQ_TIMEOUT
import org.intelehealth.core.BuildConfig
import org.intelehealth.core.interceptor.LimitedLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreNetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: LimitedLoggingInterceptor) =
        OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(HTTP_REQ_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_REQ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(HTTP_REQ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        okhttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder = Retrofit.Builder().client(okhttpClient).addConverterFactory(gsonConverterFactory)
}
