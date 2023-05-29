package com.tiamoh.uosnotice.data.repository

import com.tiamoh.uosnotice.data.api.AuthedNoticeApiHelper
import com.tiamoh.uosnotice.data.model.Notice
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Response
import java.util.Collections
import java.util.Collections.synchronizedList
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val remoteSource: AuthedNoticeApiHelper
):NoticeRepository{
    override suspend fun getNoticePage(): Response<ResponseBody> = remoteSource.getNoticePage()
    override suspend fun getNoticePortlet(): Response<ResponseBody> = remoteSource.getNoticePortlet()
    override suspend fun getPortalNoticePage(address: String, index: Int,type:Int): List<Notice> {
        val response = remoteSource.getPortalNoticePage("$address&pageIndex=$index")
        response.body()?.let { it ->
            val bodyParsed = Jsoup.parse(it.string())
            return when(type){
                1->{
                    getHomePageNotice(bodyParsed)
                }
                else -> {
                    ArrayList<Notice>()
                }
            }

        }
        return ArrayList<Notice>()
    }

    private fun getHomePageNotice(bodyParsed:Document):List<Notice>{
        val notices = synchronizedList(mutableListOf<Notice>())
        val ul = bodyParsed.getElementsByClass("brd-lstp1").first()
        ul?.let {
            for(li in ul.children()){
                val no = li.getElementsByClass("num").first()?.text()
                if(no!=null){// 고정 공지가 아닌 일반 공지 항목
                    val sequence:String
                    val title:String
                    val writer:String
                    val date:String
                    val url:String
                    val dataElement = li.getElementsByTag("span")
                    writer = dataElement[1].text()
                    date = dataElement[2].text()

                    val titleElement = li.getElementsByClass("ti").first()
                    titleElement?.let {titleElement->
                        val javaScriptCode =titleElement.getElementsByAttribute("href").attr("href")
                        val pattern = Regex("\\d+")
                        sequence = pattern.findAll(javaScriptCode.toString()).last().value
                        title = titleElement.text()
                        url = "https://www.uos.ac.kr/korNotice/view.do?list_id=FA1&seq=$sequence"
                        notices.add(
                            Notice(
                                no = no,
                                title = title,
                                writer = writer,
                                sequence = sequence,
                                date = date,
                                url = url
                            )
                        )
                    }
                }
            }
        }
        return notices
    }
}