package com.tiamoh.uosnotice.data.repository

import okhttp3.ResponseBody
import retrofit2.Response

interface NoticeRepository {
    suspend fun getNoticePage(): Response<ResponseBody>
    suspend fun getNoticePortlet(): Response<ResponseBody>
}