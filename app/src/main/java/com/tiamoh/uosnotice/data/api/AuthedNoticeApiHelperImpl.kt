package com.tiamoh.uosnotice.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AuthedNoticeApiHelperImpl @Inject constructor(
    private val authedNoticeApiService: AuthedNoticeApiService
    ):AuthedNoticeApiHelper{
    override suspend fun getNoticePage():Response<ResponseBody> {
        return authedNoticeApiService.getNoticePage()
    }
    override suspend fun getNoticePortlet(): Response<ResponseBody>{
        return authedNoticeApiService.getNoticePortlet()
    }
}