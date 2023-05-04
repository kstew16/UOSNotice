package com.tiamoh.uosnotice.data.repository

import com.tiamoh.uosnotice.data.api.AuthedNoticeApiHelper
import com.tiamoh.uosnotice.data.model.Notice
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Response
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val remoteSource: AuthedNoticeApiHelper
):NoticeRepository{
    override suspend fun getNoticePage(): Response<ResponseBody> = remoteSource.getNoticePage()
    override suspend fun getNoticePortlet(): Response<ResponseBody> = remoteSource.getNoticePortlet()
    override suspend fun getPortalNoticePage(address: String, index: Int,type:Int): List<Notice> {
        val notices = listOf<Notice>()
        val response = remoteSource.getPortalNoticePage("$address&pageIndex=$index")
        response.body()?.let {
            val bodyParsed = Jsoup.parse(it.string())
            when(type){
                1->{
                    val ul = bodyParsed.getElementsByClass("brd-lstp1").first()
                    ul?.let {
                        for(li in ul.children()){
                            val no = li.getElementsByClass("num").first()?.text()
                            if(no!=null){

                            }
                        }
                    }
                }
                2->{

                }
                3->{

                }
                else -> {
                    //에러임
                }
            }

        }

        return notices
    }
}