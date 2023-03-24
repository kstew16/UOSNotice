package com.tiamoh.uosnotice.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

interface NoticeRepository {
    suspend fun getLoginSession():Response<ResponseBody>
    suspend fun postAccountInfo(
        id: MultipartBody.Part,
        pw: MultipartBody.Part,
        loginType: MultipartBody.Part
    ):Response<ResponseBody>
    suspend fun getNoticePage(): Response<ResponseBody>
    suspend fun getNoticePortlet(): Response<ResponseBody>
}