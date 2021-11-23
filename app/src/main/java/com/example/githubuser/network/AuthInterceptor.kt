package com.example.githubuser.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Intercepts and adds Token or API Key
 */
class AuthInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val requestBuilder: Request.Builder = original.newBuilder()
            .header("Authorization", apiKey)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}