package com.skithub.resultdear.database.network;

import android.util.Base64;
import android.util.Log;

import com.skithub.resultdear.BuildConfig;
import com.skithub.resultdear.utils.TryAgainAlert;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofitForIp,retrofit;
    private static final String baseUrl="http://216.10.251.59/";
    //private static final String baseUrl="http://192.168.0.103/lotteryresultpro/";
    private static final String AUTH= "Basic "+ Base64.encodeToString((BuildConfig.USER_NAME+":"+BuildConfig.USER_PASSWORD).getBytes(),Base64.NO_WRAP);

    public static synchronized Retrofit getApiClientForIp(){
        if (retrofitForIp==null){
            retrofitForIp= new Retrofit
                    .Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitForIp;
    }

    private static HttpLoggingInterceptor httpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log (String message)
                    {
                        Log.d("Retrofit", "log: http log: " + message);
                    }
                } );
        httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public static synchronized Retrofit getApiClient(){
        if (retrofit==null){


            OkHttpClient okHttpClient=new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(httpLoggingInterceptor())
                    .addInterceptor(
                            chain -> {
                                Request original =chain.request();
                                Request.Builder requestBuilder=original.newBuilder()
                                        .addHeader("Authorization",AUTH)
                                        .method(original.method(), original.body());
                                Request request=requestBuilder.build();
                                return chain.proceed(request);
                            }
                    )
                    .build();
            retrofit= new Retrofit
                    .Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }


}
