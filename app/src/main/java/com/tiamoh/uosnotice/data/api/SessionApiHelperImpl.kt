package com.tiamoh.uosnotice.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class SessionApiHelperImpl @Inject constructor(
    private val sessionApiService: SessionApiService
):SessionApiHelper{
    override suspend fun checkPortalSession(): Response<ResponseBody>
    = sessionApiService.checkPortalSession()

    override suspend fun checkStorySession(): Response<ResponseBody>
    = sessionApiService.checkStorySession()

    override suspend fun postAccountInfo(
        id: MultipartBody.Part,
        pw: MultipartBody.Part,
        loginType: MultipartBody.Part
    ): Response<ResponseBody>
    = sessionApiService.postAccountInfo(id, pw, loginType)

}