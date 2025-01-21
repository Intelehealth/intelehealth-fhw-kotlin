package org.intelehealth.common.service

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.intelehealth.common.helper.PreferenceHelper
import org.intelehealth.common.helper.PreferenceHelper.Companion.JWT_AUTH_TOKEN
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(private val preferenceHelper: PreferenceHelper) : Interceptor {
    private val token: String? = preferenceHelper.get(JWT_AUTH_TOKEN)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken: String? = preferenceHelper.get(JWT_AUTH_TOKEN)
        return authToken?.let {
            val original: Request = chain.request()
            val builder: Request.Builder = original.newBuilder()
                .header("Authorization", "Bearer $authToken")
            val request: Request = builder.build()
            return@let chain.proceed(request)
        } ?: chain.proceed(chain.request())
    }

    fun hasToken() = !token.isNullOrEmpty()
}
