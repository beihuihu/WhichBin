package com.example.whichbin.http

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import okhttp3.ResponseBody
import retrofit2.http.GET



interface ClassicService {
    @GET("model/latest")
    fun getModelVersion(): Call<LatestModel>

    @GET("ranking")
    fun getRanking(): Call<RankingResult>

    @POST("recognize/mobilenet_v2")
    @Multipart
    fun getResult(
        @Part part: MultipartBody.Part
    ): Call<Classification>

    @GET("reward")
    fun getReward(): Call<LoginResult>

    @POST("model/predict")
    @Multipart
    fun getScores(
        @Part part: MultipartBody.Part
    ): Call<Scores>

    @GET("{url}")
    fun loadModel(
        @Path("url") url: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @FieldMap params: Map<String, String>
    ): Call<LoginResult>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @FieldMap params: Map<String, String>
    ): Call<RegisterResult>

    @POST("feedback")
    @Multipart
    fun submitFeedback(
        @Part("id") id: Int,
        @Part("class") garbageClass: String,
        @Part part: MultipartBody.Part
    ): Call<SubmitResult>

}