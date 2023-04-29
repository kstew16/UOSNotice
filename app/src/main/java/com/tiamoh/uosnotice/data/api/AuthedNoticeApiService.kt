package com.tiamoh.uosnotice.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AuthedNoticeApiService {
    @GET("portal/default/test/tesr_stu_default.page")
    suspend fun getNoticePage(): Response<ResponseBody>
    @GET("https://portal.uos.ac.kr/uos/GA07.face?flag_group=STU&lang_knd=ko&userAgent=Chrome&isMobile=false&")
    suspend fun getNoticePortlet(): Response<ResponseBody>
}