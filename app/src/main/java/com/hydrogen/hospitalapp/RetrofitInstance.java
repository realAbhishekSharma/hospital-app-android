package com.hydrogen.hospitalapp;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance{

    public static Retrofit getInstance(String BASE_URL){
        String Token ="l";

        HttpLoggingInterceptor loggingInterceptor= new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient= new OkHttpClient.Builder().addInterceptor(chain ->
            chain.proceed(
            chain.request().newBuilder().header("Authorization", Token).header("Content-type", "application/json").build())
        ).addInterceptor(loggingInterceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        /*return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();*/

        return  new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
