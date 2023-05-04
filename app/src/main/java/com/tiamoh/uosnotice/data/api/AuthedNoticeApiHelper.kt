package com.tiamoh.uosnotice.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Path

interface AuthedNoticeApiHelper {
    suspend fun getNoticePage():Response<ResponseBody>
    suspend fun getNoticePortlet(): Response<ResponseBody>
    suspend fun getPortalNoticePage(
        address:String
    ): Response<ResponseBody>
}