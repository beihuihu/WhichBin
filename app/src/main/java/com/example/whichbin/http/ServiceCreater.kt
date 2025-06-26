package com.example.whichbin.http

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.jvm.internal.Intrinsics


object ServiceCreater {
//可替换为自己的网页
    private const val Base_URL="http://150.158.25.44:5000/"

    private val retrofit=Retrofit.Builder()
        .baseUrl(Base_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun<T> creat(cls:Class<T>, context: Context):T{
        return Retrofit.Builder().baseUrl(Base_URL).client(
            OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(AddCookiesInterceptor(context)).build()
        ).addConverterFactory(GsonConverterFactory.create()).build().create(cls)
    }

    fun <T> creatLogin(cls: Class<T>?, context: Context): T {
        return Retrofit.Builder().baseUrl(Base_URL).client(
            OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(SaveCookiesInterceptor(context)).build()
        ).addConverterFactory(GsonConverterFactory.create()).build().create(cls)
    }

    fun <T> creatModel(cls: Class<T>?, context: Context?): T {
        return Retrofit.Builder().baseUrl(Base_URL).client(
            OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()
        ).build().create(cls)
    }

    fun <T> creatRegister(cls: Class<T>?): T {
        return Retrofit.Builder().baseUrl(Base_URL).client(
            OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()
        ).addConverterFactory(GsonConverterFactory.create()).build().create(cls)
    }

}