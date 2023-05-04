package com.tiamoh.uosnotice.data.repository

import com.tiamoh.uosnotice.data.api.dto.NoticeDTO
import com.tiamoh.uosnotice.data.model.Notice
import okhttp3.ResponseBody
import retrofit2.Response

interface NoticeRepository {
    suspend fun getNoticePage(): Response<ResponseBody>
    suspend fun getNoticePortlet(): Response<ResponseBody>
    suspend fun getPortalNoticePage(
        address:String,
        index:Int,
        type:Int
    ):List<Notice>
}