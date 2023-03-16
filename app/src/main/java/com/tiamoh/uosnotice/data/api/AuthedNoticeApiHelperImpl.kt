package com.tiamoh.uosnotice.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
class AuthedNoticeApiHelperImpl @Inject constructor(
    private val authedNoticeApiService: AuthedNoticeApiService
    ):AuthedNoticeApiHelper{
    override suspend fun getLoginSession(): Response<ResponseBody> = authedNoticeApiService.getLoginSession()
    override suspend fun postAccountInfo(
        id: MultipartBody.Part,
        pw: MultipartBody.Part,
        loginType: MultipartBody.Part
    ): Response<ResponseBody> = authedNoticeApiService.postAccountInfo(id,pw,loginType)
    override suspend fun getNotices():Response<Void> {
        return authedNoticeApiService.getNotices()
    }
}