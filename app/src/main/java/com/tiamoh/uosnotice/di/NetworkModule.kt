package com.tiamoh.uosnotice.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tiamoh.uosnotice.BuildConfig
import com.tiamoh.uosnotice.data.api.*
import com.tiamoh.uosnotice.data.repository.NoticeRepository
import com.tiamoh.uosnotice.data.repository.NoticeRepositoryImpl
import com.tiamoh.uosnotice.data.repository.SessionRepository
import com.tiamoh.uosnotice.data.repository.SessionRepositoryImpl
import com.tiamoh.uosnotice.util.ProxyCookieManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val gson : Gson = GsonBuilder()
        .setLenient()
        .create()

    private val syncCookieManager = ProxyCookieManager()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(syncCookieManager))
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

    @Provides
    @Singleton
    fun provideSessionRepository(sessionRepository: SessionRepositoryImpl): SessionRepository =
        sessionRepository

    @Provides
    @Singleton
    fun provideSessionApiService(retrofit: Retrofit): SessionApiService =
        retrofit.create(SessionApiService::class.java)

    @Provides
    @Singleton
    fun provideSessionApiHelper(sessionApiHelper: SessionApiHelperImpl): SessionApiHelper =
        sessionApiHelper

}