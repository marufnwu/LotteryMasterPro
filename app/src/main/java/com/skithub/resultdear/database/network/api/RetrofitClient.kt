package com.skithub.resultdear.database.network.api

import com.google.gson.GsonBuilder
import com.skithub.resultdear.BuildConfig

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        const val HEADER_CACHE_CONTROL = "Cache-Control-agent"
        const val HEADER_PRAGMA = "Pragma-agent"
        const val BASE_URL = "http://192.168.0.103/lotteryresultpro/"

        //const val BASE_URL = "http://216.10.251.59/agent/api/"
        @Volatile
        private var iRetrofitApiCall: IRetrofitApiCall? = null
        private val LOCK = Any()

        operator fun invoke() = iRetrofitApiCall ?: synchronized(LOCK) {
            iRetrofitApiCall ?: getRetrofitClient().also {
                iRetrofitApiCall = it
            }
        }

        private fun getRetrofitClient(): IRetrofitApiCall {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.setLenient()
            val gson = gsonBuilder.create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(IRetrofitApiCall::class.java)
        }



        private fun okHttpClient(): OkHttpClient{
            return OkHttpClient.Builder()
                //.addInterceptor(httpLoggingInterceptor())
                .addNetworkInterceptor(networkInterceptor())
                .build()
        }

        private fun httpLoggingInterceptor(): HttpLoggingInterceptor {

            return HttpLoggingInterceptor().apply {

                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            }
        }

        private fun networkInterceptor(): Interceptor{
            return Interceptor.invoke {
                chain -> chain.proceed(chain.request()).also {
                    val cacheControl: CacheControl = CacheControl.Builder()
                        .maxAge(5, TimeUnit.SECONDS)
                        .build()

                it.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build()

            }
            }
        }



    }
}