package org.intelehealth.app.feature.video.httpclient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.intelehealth.app.BuildConfig
import java.util.concurrent.TimeUnit

/**
 * Created by Vaghela Mithun R. on 30-06-2023 - 00:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class OkHttpClientProvider {
    fun provideOkHttpClient(): OkHttpClient = provideOkHttpBuilder(provideHttpLoggingInterceptor()).build()

    private fun provideOkHttpBuilder(interceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS).writeTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .addInterceptor(interceptor)

    private fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    companion object {
        const val TIMEOUT_SECOND = 60L
    }
}
