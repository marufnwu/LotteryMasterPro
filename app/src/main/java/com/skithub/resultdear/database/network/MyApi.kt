package com.skithub.resultdear.database.network

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.skithub.resultdear.BuildConfig
import com.skithub.resultdear.model.*
import com.skithub.resultdear.model.response.*
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
import java.util.concurrent.TimeUnit

interface MyApi {


    @GET("get_similar_lottery_number_list.php?")
    suspend fun findSimilarLotteryNumberList(
        @Query("PageNumber") pageNumber: String,
        @Query("ItemCount") itemCount: String,
        @Query("LotteryNumber") lotteryNumber: String
    ): Response<LotteryNumberResponse>

    @GET("get_lottery_number_list_by_lottery_number.php?")
    suspend fun getLotteryNumberListUsingLotteryNumber(
        @Query("LotteryNumber") lotteryNumber: String
    ): Response<LotteryNumberResponse>

    @GET("get_middle_list_by_lottery_number.php?")
    suspend fun getMiddleListUsingLotteryNumber(
        @Query("LotteryNumber") lotteryNumber: String
    ): Response<LotteryNumberResponse>

    @GET("get_2nd_middle_list_by_lottery_number.php?")
    suspend fun get2ndMiddleListUsingLotteryNumber(
        @Query("LotteryNumber") lotteryNumber: String
    ): Response<LotteryNumberResponse>

    @GET("get_1st_middle_list_by_lottery_number.php?")
    suspend fun get1stMiddleListUsingLotteryNumber(
        @Query("LotteryNumber") lotteryNumber: String
    ): Response<LotteryNumberResponse>

    @GET("get_lottery_number_list_by_win_date_time.php?")
    suspend fun getLotteryNumberListByDateTime(
        @Query("WinDate") winDate: String,
        @Query("WinTime") winTime: String,
        @Query("userId") userId: String
    ): Response<LotteryNumberResponse>

    @GET("get_lottery_number_list_by_win_date_slot.php?")
    suspend fun getLotteryNumberListByDateSlot(
        @Query("WinDate") winDate: String,
        @Query("SlotId") slotId: Int,
        @Query("userId") userId: String
    ): Response<LotteryNumberResponse>

    @GET("get_lottery_number_list_by_win_type.php?")
    suspend fun getLotteryNumberListByWinType(
        @Query("PageNumber") pageNumber: String,
        @Query("ItemCount") itemCount: String,
        @Query("WinType") winType: String
    ): Response<LotteryNumberResponse>

    @GET("get_duplicate_lottery_number_list.php?")
    suspend fun getDuplicateLotteryNumberList(
        @Query("PageNumber") pageNumber: String,
        @Query("ItemCount") itemCount: String
    ): Response<LotteryNumberResponse>

    @GET("get_lottery_number_plays_more.php?")
    suspend fun getMiddlePlaysMoreNumberList(
        @Query("PageNumber") pageNumber: String,
        @Query("ItemCount") itemCount: String
    ): Response<LotteryNumberResponse>

    @GET("get_two_nd_middle_plays_more.php?")
    suspend fun gettwoNdMiddlePlaysMoreList(
        @Query("PageNumber") pageNumber: String,
        @Query("ItemCount") itemCount: String
    ): Response<LotteryNumberResponse>

    @GET("get_one_st_middle_plays_more.php?")
    suspend fun get1stNdMiddlePlaysMoreList(
        @Query("PageNumber") pageNumber: String,
        @Query("ItemCount") itemCount: String
    ): Response<LotteryNumberResponse>

    @GET("get_lottery_number_less.php?")
    suspend fun getMiddleLessNumberList(
        @Query("PageNumber") pageNumber: String,
        @Query("ItemCount") itemCount: String
    ): Response<LotteryNumberResponse>

    @GET("get_old_result_in_user_app.php?")
    suspend fun getLotteryResultList(
        @Query("PageNumber") pageNumber: String,
        @Query("ItemCount") itemCount: String
    ): Response<LotteryNumberResponse>

    @GET("get_bumper_result_list.php?")
    suspend fun getBumperLotteryResultList(
        @Query("PageNumber") pageNumber: String,
        @Query("ItemCount") itemCount: String
    ): Response<LotteryPdfResponse>

    @GET("get_paid_for_contact_info.php?")
    suspend fun getPaidContInfoList(
        @Query("pageId") pageNumber: String,
        @Query("userId") itemCount: String
    ): Response<PaidForContactModel>

    @GET("get_special_number_in_user_app.php?")
    suspend fun getSpecialNumberList(
        @Query("userId") itemCount: String
    ): Response<SpecialNumberModel>

    @GET("get_video_in_user_app.php?")
    suspend fun getVideoList(
        @Query("userId") itemCount: String
    ): Response<VideoTutorResponse>

    @GET("get_video_in_result_info.php?")
    suspend fun getVideoListInResultInfo(
        @Query("userId") itemCount: String
    ): Response<VideoTutorResponse>

    @GET("get_ads_info.php?")
    suspend fun getAdsInfo(): Response<AdsImageResponse>


    @GET("get_service_info.php?")
    suspend fun getServiceInfo(
        @Query("userId") userId: String
    ): Response<ServiceInfoModel>

    @GET("get_home_tutorial.php?")
    suspend fun getHomeTutorialInfo(): Response<AdsImageResponse>

    @POST("upload_user_info.php?")
    suspend fun uploadUserInfo(
        @Query("Token") token: String,
        @Query("Phone") phone: String,
        @Query("RegistrationDate") registrationDate: String,
        @Query("ActiveStatus") activeStatus: String,
        @Query("countryCode") countryCode: String
    ): Response<UserInfoResponse>

    @POST("logout_user.php?")
    suspend fun logouUser(
        @Query("userId") token: String
    ): Response<UserInfoResponse>

    @GET("get_user_info_by_token.php?")
     fun getUserInfoByToken(
        @Query("userId") userId: String,
        @Query("Token") token: String
    ): Response<UserInfoResponse>


    //Maruf's works start

    @GET("api/helper.getBanner.php")
    suspend fun getBanner(
        @Query("bannerName") bannerName: String,
    ): Response<BannerRes>

    @GET("api/helper.getWhatsapp.php")
    suspend fun getWhatsapp(
        @Query("place") place: String,
    ): Response<WhatsappResponse>

    @GET("api/audio.getAudio.php")
     fun getAudio(
        @Query("name") name: String,
    ): Call<AudioResponse>


     @GET("api/helper.addDeviceMetadata.php")
     fun addDeviceMetadata(
        @Query("userId") userId: String,
        @Query("phone") phone: String,
        @Query("versionCode") versionCode: Int,
        @Query("versionName") versionName: String,
        @Query("androidVersion") androidVersion: String,
        @Query("device") device: String,
        @Query("manufacturer") manufacturer: String,
        @Query("screenDensity") screenDensity: String,
        @Query("screenSize") screenSize: String
    ): Call<LotterySlotResponse>


     @GET("api/helper.searchDeviceMetadata.php")
     fun searchDeviceMetadata(
        @Query("androidVersion") androidVersion: String,
        @Query("device") device: String,
        @Query("manufacturer") manufacturer: String,
        @Query("screenDensity") screenDensity: String,
        @Query("screenSize") screenSize: String
    ): Call<MetadataSearchResponse>


    @FormUrlEncoded
    @POST("api/lottery.getLotteryResultTime.php")
    fun getLotteryResultTime(
        @Field("checkActive") checkActive: Boolean
    ): Call<LotterySlotResponse>

    //Maruf's work end here

    companion object {

        @Volatile
        private var myApiInstance: MyApi? = null
        private val LOCK = Any()

        operator fun invoke() = myApiInstance ?: synchronized(LOCK) {
            myApiInstance ?: createClient().also {
                myApiInstance = it
            }
        }


        private fun createClient(): MyApi {
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




            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
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
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(MyApi::class.java)
        }


    }


}