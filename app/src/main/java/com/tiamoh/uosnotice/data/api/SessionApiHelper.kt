package com.tiamoh.uosnotice.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

interface SessionApiHelper {
    suspend fun checkPortalSession(): Response<ResponseBody>
    suspend fun checkStorySession(): Response<ResponseBody>
    suspend fun postAccountInfo(
        id: MultipartBody.Part,
        pw: MultipartBody.Part,
        loginType: MultipartBody.Part
    ): Response<ResponseBody>
}