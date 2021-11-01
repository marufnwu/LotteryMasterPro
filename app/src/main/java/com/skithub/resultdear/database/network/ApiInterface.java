package com.skithub.resultdear.database.network;

import com.google.gson.JsonElement;
import com.skithub.resultdear.model.response.BannerRes;
import com.skithub.resultdear.model.response.BannerResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("get_user_app_home.php?")
    Call<JsonElement> getDeshCount(@Query("userid") String userid,@Query("token") String token,@Query("current_version") String current_version);

    @GET("get_paid_for_contact.php?")
    Call<JsonElement> getPaidforContact(@Query("pageId") String pageId,@Query("userId") String userId);

    @GET("get_result_viewer.php?")
    Call<JsonElement> getResultViewer(@Query("WinDate") String WinDate,@Query("WinTime") String WinTime, @Query("userId") String userId);

    @GET("api/helper.getBanner.php")
    Call<BannerResponse> getBanner(@Query("bannerName") String bannerName);
}
