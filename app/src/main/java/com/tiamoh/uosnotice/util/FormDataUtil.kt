package com.tiamoh.uosnotice.util

import okhttp3.MultipartBody

object FormDataUtil {
    fun getBody(key: String, value: Any): MultipartBody.Part {
        return MultipartBody.Part.createFormData(key, value.toString())
    }
}