package com.example.movieappmvvmwithpagination.data.api

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

const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
const val FIRST_PAGE = 1
const val POSTS_PER_PAGE = 20
const val API_KEY = "api_key"