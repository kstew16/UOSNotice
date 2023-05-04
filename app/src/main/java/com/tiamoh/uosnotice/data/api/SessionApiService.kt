package com.tiamoh.uosnotice.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SessionApiService {
    @Multipart
    @POST("https://portal.uos.ac.kr/user/loginProcess.face")
    suspend fun postAccountInfo(
        @Part id: MultipartBody.Part,
        @Part pw: MultipartBody.Part,
        @Part loginType: MultipartBody.Part
    ): Response<ResponseBody>

    @GET("https://portal.uos.ac.kr/uos/GA07.face?flag_group=STU&lang_knd=ko&userAgent=Chrome&isMobile=false&")
    suspend fun checkPortalSession(): Response<ResponseBody>

    @GET("https://uostory.uos.ac.kr/site/main/index003")
    suspend fun checkStorySession():Response<ResponseBody>

}