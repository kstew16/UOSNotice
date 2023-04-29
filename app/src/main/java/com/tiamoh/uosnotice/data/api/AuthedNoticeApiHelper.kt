package com.tiamoh.uosnotice.data.api

import okhttp3.ResponseBody
import retrofit2.Response

interface AuthedNoticeApiHelper {
    suspend fun getNoticePage():Response<ResponseBody>
    suspend fun getNoticePortlet(): Response<ResponseBody>
}