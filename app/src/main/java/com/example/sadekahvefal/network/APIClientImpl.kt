package com.example.sadekahvefal.network
import com.example.sadekahvefal.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

const val CONNECTION_TIMEOUT_SEC = 5 * 60L

interface APIClient {
    val apiCollect: Apies
}

@Singleton
class APIClientImpl @Inject constructor() : APIClient {

    override val apiCollect: Apies by lazy {
        clientCollect.create(Apies::class.java)
    }

    private val clientCollect: Retrofit by lazy {
        retrofitBuilderCollect.client(okHttpClientCollect).build()
    }

    private val stethoInterceptor: StethoInterceptor? by lazy {
        if (BuildConfig.DEBUG) StethoInterceptor() else null
    }


    private val retrofitBuilderCollect: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
    }

    private val okHttpClientCollect: OkHttpClient by lazy {
        okHttpClientBuilderCollect.addInterceptor { chain ->
            val builder = chain.request().newBuilder()
            chain.proceed(builder.build())
        }
        okHttpClientBuilderCollect.build()
    }

    private val okHttpClientBuilderCollect by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        stethoInterceptor?.let { builder.addNetworkInterceptor(it) }

        builder
    }
}