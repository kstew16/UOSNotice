package com.tiamoh.uosnotice.data.repository

import com.tiamoh.uosnotice.data.api.SessionApiHelper
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class SessionRepositoryImpl  @Inject constructor(
    private val remoteSource: SessionApiHelper
):SessionRepository{
    override suspend fun postAccountInfo(
        id: MultipartBody.Part,
        pw: MultipartBody.Part,
        loginType: MultipartBody.Part
    ): Response<ResponseBody> = remoteSource.postAccountInfo(id, pw, loginType)
    override suspend fun checkPortalSession(): Response<ResponseBody> = remoteSource.checkPortalSession()
    override suspend fun checkStorySession(): Response<ResponseBody> = remoteSource.checkStorySession()
}