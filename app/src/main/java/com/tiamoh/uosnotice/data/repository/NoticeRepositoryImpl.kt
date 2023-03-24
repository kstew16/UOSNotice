package com.tiamoh.uosnotice.data.repository

import com.tiamoh.uosnotice.data.api.AuthedNoticeApiHelper
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val remoteSource: AuthedNoticeApiHelper
):NoticeRepository{
    override suspend fun getLoginSession(): Response<ResponseBody> = remoteSource.getLoginSession()
    override suspend fun postAccountInfo(
        id: MultipartBody.Part,
        pw: MultipartBody.Part,
        loginType: MultipartBody.Part
    ): Response<ResponseBody> = remoteSource.postAccountInfo(id, pw, loginType)
    override suspend fun getNoticePage(): Response<ResponseBody> = remoteSource.getNoticePage()
    override suspend fun getNoticePortlet(): Response<ResponseBody> = remoteSource.getNoticePortlet()
    override suspend fun test():Response<ResponseBody> = remoteSource.test()
}