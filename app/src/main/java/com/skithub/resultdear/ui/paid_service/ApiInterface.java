package com.skithub.resultdear.ui.paid_service;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("json")
    Call<JsonElement> getIpInfo();

    @GET("get_service_info.php?")
    Call<JsonElement> getPostSearch(@Query("userId") String searchKey);



}
