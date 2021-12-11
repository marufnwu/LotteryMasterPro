package com.skithub.resultdear.database.network.api

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.skithub.resultdear.BuildConfig
import com.skithub.resultdear.model.*
import com.skithub.resultdear.model.response.AudioResponse
import com.skithub.resultdear.model.response.BannerRes
import com.skithub.resultdear.model.response.LotterySlotResponse
import com.skithub.resultdear.model.response.WhatsappResponse
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.URI.create
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface SecondServerApi {

    @GET("get_video_in_user_app.php?")
    suspend fun getVideoList(
        @Query("userId") itemCount: String
    ): Response<VideoTutorResponse>

    @GET("get_video_in_result_info.php?")
    suspend fun getVideoListInResultInfo(
        @Query("userId") itemCount: String
    ): Response<VideoTutorResponse>

    @GET("get_lottery_number_list_by_win_date_slot.php?")
    suspend fun getLotteryNumberListByDateSlot(
            @Query("WinDate") winDate: String,
            @Query("SlotId") slotId: Int,
            @Query("userId") userId: String
    ): Response<LotteryNumberResponse>

    @GET("get_similar_lottery_number_list.php?")
    suspend fun findSimilarLotteryNumberList(
            @Query("PageNumber") pageNumber: String,
            @Query("ItemCount") itemCount: String,
            @Query("LotteryNumber") lotteryNumber: String
    ): Response<LotteryNumberResponse>


    companion object {

        @Volatile
        private var myApiInstance: SecondServerApi? = null
        private val LOCK = Any()

        operator fun invoke() = myApiInstance ?: synchronized(LOCK) {
            myApiInstance ?: createClient().also {
                myApiInstance = it
            }
        }


        private fun createClient(): SecondServerApi {
            val AUTH: String = "Basic ${
                Base64.encodeToString(
                    ("${BuildConfig.USER_NAME}:${BuildConfig.USER_PASSWORD}").toByteArray(),
                    Base64.NO_WRAP
                )
            }"



            val interceptor = run {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.apply {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                }
            }

            val trustAllCerts: Array<TrustManager> = arrayOf(
                    object : X509TrustManager {
                        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                        }

                        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                        }


                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            var sslSocketFactory: SSLSocketFactory = sslContext.socketFactory



            var okHttpClient: OkHttpClient = OkHttpClient.Builder()
                    //.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    //.hostnameVerifier { _, _ -> true }
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .callTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    try {
                        val request = chain.request()
                        val response = chain.proceed(request)

                        response
                    }catch (e :Exception){
                        e.message?.let { Log.d("OkHttpError", it) }
                        chain.proceed(chain.request())
                    }
                }
                .addInterceptor { chain ->
                    val original: Request = chain.request()
                    val requestBuilder: Request.Builder = original.newBuilder()
                        .addHeader("Authorization", AUTH)
                        .method(original.method, original.body)
                    val request: Request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()





            val gsonBuilder = GsonBuilder()
            gsonBuilder.setLenient()
            val gson = gsonBuilder.create()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.SECOND_SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(SecondServerApi::class.java)
        }


    }


}