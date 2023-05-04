package com.tiamoh.uosnotice.data.repository

import com.tiamoh.uosnotice.data.api.AuthedNoticeApiHelper
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val remoteSource: AuthedNoticeApiHelper
):NoticeRepository{
    override suspend fun getNoticePage(): Response<ResponseBody> = remoteSource.getNoticePage()
    override suspend fun getNoticePortlet(): Response<ResponseBody> = remoteSource.getNoticePortlet()

}