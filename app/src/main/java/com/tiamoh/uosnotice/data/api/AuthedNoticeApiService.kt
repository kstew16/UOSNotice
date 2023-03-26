package com.tiamoh.uosnotice.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AuthedNoticeApiService {
    @GET("/user/login.face")
    suspend fun getLoginSession(): Response<ResponseBody>


    @Multipart
    //@Headers("Content-Type: multipart/form-data")
    @POST("user/loginProcess.face")
    suspend fun postAccountInfo(
        @Part id: MultipartBody.Part,
        @Part pw: MultipartBody.Part,
        @Part loginType: MultipartBody.Part
    ): Response<ResponseBody>

    @GET("portal/default/test/tesr_stu_default.page")
    suspend fun getNoticePage(): Response<ResponseBody>

    @GET("https://portal.uos.ac.kr/uos/GA07.face?flag_group=STU&lang_knd=ko&userAgent=Chrome&isMobile=false&")
    suspend fun getNoticePortlet(): Response<ResponseBody>

    //@GET("https://www.uos.ac.kr/korNotice/list.do?list_id=FA1")
    //suspend fun test():Response<ResponseBody>
}