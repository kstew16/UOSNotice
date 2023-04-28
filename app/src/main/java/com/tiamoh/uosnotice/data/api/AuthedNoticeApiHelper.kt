package com.tiamoh.uosnotice.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

interface AuthedNoticeApiHelper {
    suspend fun getLoginSession():Response<ResponseBody>
    suspend fun postAccountInfo(
        id: MultipartBody.Part,
        pw: MultipartBody.Part,
        loginType: MultipartBody.Part
    ):Response<ResponseBody>
    suspend fun getNoticePage():Response<ResponseBody>
    suspend fun getNoticePortlet(): Response<ResponseBody>
    /*
    suspend fun loginToUoStory(
        id: MultipartBody.Part,
        pw: MultipartBody.Part,
        loginType: MultipartBody.Part
    ): Response<ResponseBody>
    suspend fun test(): Response<ResponseBody>

     */
    //suspend fun test():Response<ResponseBody>
}