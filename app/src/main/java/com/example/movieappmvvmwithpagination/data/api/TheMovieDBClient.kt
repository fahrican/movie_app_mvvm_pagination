package com.example.movieappmvvmwithpagination.data.api

import com.example.movieappmvvmwithpagination.data.constant.API_KEY
import com.example.movieappmvvmwithpagination.data.constant.BASE_URL
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object TheMovieDBClient {

    fun getClient(): ITheMovieDB {
        val requestInterceptor = Interceptor { chain ->
            val url: HttpUrl = chain.request()
                .url
                .newBuilder()
                .addQueryParameter(API_KEY, TMDB_API_KEY)
                .build()

            val request: Request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }
        val okHttpClient = createOkHttpClient(requestInterceptor)

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ITheMovieDB::class.java)
    }

    // Prepare to log HTTP request
    private fun createOkHttpClient(requestInterceptor: Interceptor): OkHttpClient {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(generateInterceptorCallback())
            .addInterceptor(logger)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    // Here gets the HTTP request logged to Logcat
    private fun generateInterceptorCallback(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest: Request = chain.request()
                val request = originalRequest.newBuilder().header(API_KEY, TMDB_API_KEY).build()
                return chain.proceed(request)
            }
        }
    }
}
