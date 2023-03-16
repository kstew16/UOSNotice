package com.tiamoh.uosnotice.data.repository

import com.tiamoh.uosnotice.data.api.AuthedNoticeApiHelper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
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
    override suspend fun getNotices(typeNo: Int): Response<Void> = remoteSource.getNotices()
}