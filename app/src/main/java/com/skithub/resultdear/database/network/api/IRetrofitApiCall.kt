package com.skithub.resultdear.database.network.api

import com.skithub.resultdear.model.response.BannerRes
import com.skithub.resultdear.model.response.BannerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface IRetrofitApiCall {

    @GET("api/helper.getBanner.php?")
    fun getBanner(@Query("bannerName") bannerName: String?): Call<BannerRes>

}