package com.skithub.resultdear.utils;

import android.util.Log;

import com.skithub.resultdear.database.network.ApiInterface;
import com.skithub.resultdear.database.network.MyApi;
import com.skithub.resultdear.model.response.BannerRes;
import com.skithub.resultdear.model.response.BannerResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallBanner {


    public static void getBanner(ApiInterface myApi){

        myApi.getBanner("VipPremium1")
                .enqueue(new Callback<BannerResponse>() {
                    @Override
                    public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                        Log.d("Baaal", response.body().getMsg());
                    }

                    @Override
                    public void onFailure(Call<BannerResponse> call, Throwable throwable) {

                    }
                });
    }
}
