package com.tiamoh.uosnotice.data.repository

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
// 세션이 레포지토리로 관리될 필요가 없는데 왜 이렇게 함? 다시하셈
interface SessionRepository {
    suspend fun checkPortalSession(): Response<ResponseBody>
    suspend fun checkStorySession(): Response<ResponseBody>
    suspend fun postAccountInfo(
        id: MultipartBody.Part,
        pw: MultipartBody.Part,
        loginType: MultipartBody.Part
    ): Response<ResponseBody>
}