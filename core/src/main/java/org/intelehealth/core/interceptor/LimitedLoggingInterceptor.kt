package org.intelehealth.core.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 13-02-2025 - 17:41.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class LimitedLoggingInterceptor @Inject constructor(
    private val loggingInterceptor: HttpLoggingInterceptor
) : Interceptor {
    companion object {
        const val MAX_LOG_SIZE: Long = 1024L * 1024L // 1 MB
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())

        response.body?.let {
            val source = response.body!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer.clone()

            val limitedBuffer = if (buffer.size > MAX_LOG_SIZE) {
                Buffer().apply { write(buffer, MAX_LOG_SIZE) }
            } else {
                buffer
            }

            it.contentType()?.charset()?.let { it1 -> limitedBuffer.readString(it1) }
            loggingInterceptor.intercept(chain)
        }

        return response
    }
}
