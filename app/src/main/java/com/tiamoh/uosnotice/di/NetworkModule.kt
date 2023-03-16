package com.tiamoh.uosnotice.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tiamoh.uosnotice.BuildConfig
import com.tiamoh.uosnotice.data.api.AuthedNoticeApiHelper
import com.tiamoh.uosnotice.data.api.AuthedNoticeApiHelperImpl
import com.tiamoh.uosnotice.data.api.AuthedNoticeApiService
import com.tiamoh.uosnotice.data.repository.NoticeRepository
import com.tiamoh.uosnotice.data.repository.NoticeRepositoryImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.CookieManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val gson : Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.UOS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun provideNoticeRepository(noticeRepository: NoticeRepositoryImpl): NoticeRepository =
        noticeRepository

    @Provides
    @Singleton
    fun provideNoticeApiService(retrofit: Retrofit): AuthedNoticeApiService =
        retrofit.create(AuthedNoticeApiService::class.java)

    @Provides
    @Singleton
    fun provideNoticeApiHelper(noticeApiHelper: AuthedNoticeApiHelperImpl): AuthedNoticeApiHelper =
        noticeApiHelper

}