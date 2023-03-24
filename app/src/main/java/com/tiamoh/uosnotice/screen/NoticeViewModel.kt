package com.tiamoh.uosnotice.screen

import android.util.Log
import androidx.lifecycle.*
import com.tiamoh.uosnotice.data.model.Notice
import com.tiamoh.uosnotice.data.repository.NoticeRepository
import com.tiamoh.uosnotice.util.FormDataUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.jsoup.Connection
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository
):ViewModel() {
    private var _list = MutableLiveData<List<Notice>>()
    val list: LiveData<List<Notice>>
        get() = _list
    private var _currentType:Int
    private var _filterText:String
    private val defaultType = 1

    private var lastLoginID = ""
    private var _failedLoginCount = MutableLiveData<Int>()
    val failedLoginCount: LiveData<Int>
        get() = _failedLoginCount
    private val noticeURLList = Array(7){""}.apply {
        // 홈페이지공지
        this[1] = "https://www.uos.ac.kr/korNotice/list.do?list_id=FA1"
        // 학사공지는 계정 정보가 필요하기때문에 로그인 후에 세팅
        // 장학공지
        this[3] = "https://scholarship.uos.ac.kr/scholarship/notice/notice/list.do?brdBbsseq=1"
        this[4] = ""
    }
    // 필터되기 전 리스트
    private val loadedNotice = ArrayList<Notice>()


    init {
        Log.d("vm","init vm")
        _currentType = defaultType
        _filterText = ""
        _failedLoginCount.postValue(0)
        //loadNotices()
        //filterNoticeBy(_filterText)
        loginAndCrawl("kstew16","projectPassword16!")

    }

    fun loginAndCrawl(id:String, pw:String) = viewModelScope.launch(Dispatchers.IO){
        if(id!=lastLoginID) _failedLoginCount.postValue(0)

        val handler = CoroutineExceptionHandler{_,throwable ->
            throwable.message?.let { Log.d("vm", it) }
        }
        withContext(Dispatchers.IO+handler){
            val formId = FormDataUtil.getBody("ssoId",id)
            val formPw = FormDataUtil.getBody("password",pw)
            val formLoginType = FormDataUtil.getBody("loginType","normal")

            val loginResponse = noticeRepository.postAccountInfo(formId,formPw,formLoginType)
            if(loginResponse.headers().size!=7){
                _failedLoginCount.postValue(_failedLoginCount.value?.plus(1) ?: 1)
            }else{
                _failedLoginCount.postValue(0)
                // 파싱 로직
                getListOfNotices(_currentType)
                //val response = noticeRepository.getNoticePortlet()
                //response.body()?.let { fillNoticeURLs(it) }
            }
        }
    }
/*
    fun loadNotices(){
        Log.d("vm","postNotice $_currentType type")
        _list.postValue(getListOfNotices(_currentType))
    }

 */
   fun filterNoticeBy(searchText:String){
        Log.d("vm","filter by $searchText, typeNO $_currentType")
        _filterText = searchText
        val filteredList = ArrayList<Notice>()
        if(_filterText == "") filteredList.addAll(loadedNotice)
        else loadedNotice.forEach { notice->
            if(notice.title.contains(_filterText.lowercase())||
                        notice.date.contains(_filterText.lowercase())||
                        notice.writer.contains(_filterText.lowercase())
            ) filteredList.add(notice)
        }
        _list.postValue(filteredList)
    }


    fun selectNoticeType(typeNo:Int){
        _currentType = typeNo
        Log.d("vm","set TypeNo $_currentType type")
        filterNoticeBy(_filterText)
    }

    // Todo : 파싱해서 만들기 : 코루틴
    private fun getListOfNotices(typeNo:Int){
        val handler = CoroutineExceptionHandler{_,throwable ->
            throwable.message?.let { Log.d("vm", it) }
        }
        viewModelScope.launch(Dispatchers.IO+handler){
            val loginResponse = noticeRepository.getNoticePage()
            if(!loginResponse.isSuccessful || loginResponse.headers().size!=7){
                throw Exception("SessionError")
            }else{
                // 로그인 성공
                val response = noticeRepository.getNoticePortlet()
                // 로그인 이후에 알아낼 수 있는 url 등록
                response.body()?.let { fillNoticeURLs(it) }
                // url 을 통해서 공지사항 로드 후 등록
                when(typeNo){
                    1->{
                        loadedNotice.clear()

                        //val doc = Jsoup.connect(noticeURLList[1])
                        val doc = Jsoup.connect("https://www.uos.ac.kr/korNotice/list.do?list_id=FA1")
                            //.timeout(3000).ignoreHttpErrors(true)
                            .header(
                                "Accept",
                                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
                            )
                            .header("Upgrade-Insecure-Requests","1")
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                            .header("sec-ch-ua", "Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111")
                            .header("sec-ch-ua-mobile","?0")
                            .header("sec-ch-ua-platform","\"Windows\"")
                            .get()
                        //val doc = Jsoup.parse(response.toString())
                        val notices = doc.getElementsByClass("li")
                        notices.forEach { noticeListItem ->
                            val titleClass = noticeListItem.getElementsByClass("ti")
                            var url = ""
                            var title = ""
                            for(i in titleClass.indices){
                                if(titleClass[i]!=null){
                                    val (sort,seq) = with(titleClass[i].getElementsByAttribute("href").toString()){
                                        this.substring(this.indexOf('('),this.indexOf(')')).split(",").map { wrappedParameter->
                                            wrappedParameter.substring(1,wrappedParameter.length-1)
                                        }
                                    }
                                    url = "https://www.uos.ac.kr/korNotice/view.do?list_id=FA1&seq=$seq&sort=$sort&pageIndex=2&searchCnd=&searchWrd=&cate_id=&viewAuth=Y&writeAuth=N&board_list_num=10&lpageCount=12&menuid=2000005009002000000"
                                    title = titleClass[i].data()
                                    break
                                }
                            }
                            val dataClass = noticeListItem.getElementsByClass("da")
                            var writer = ""
                            var date = ""
                            dataClass.forEachIndexed { index, element ->
                                when(index){
                                    0 -> {writer = element.data()}
                                    1 -> {date = element.data()}
                                }
                            }
                            if(title!="" && url != "" && writer !="" && date != ""){
                                loadedNotice.add(Notice(
                                    title,title, writer, date, url
                                ))
                            }
                        }
                        filterNoticeBy(_filterText)
                    }
                    else->{
                        TODO("Not Implemented yet")
                    }
                }
            }
        }
        // Repository 에게 데이터를 요청하고, 응답받은 데이터를 가공하여 list 화하여 반환하는것이 주 목표
        /*
        Log.d("vm","getList $typeNo type")
        val sampleList = ArrayList<Notice>()
        for(i in 1 until 1000){
            sampleList.add(
                Notice(
                    title = "$typeNo 타입의 $i 번째 공지를 표시하는 중입니다.",
                    writer = if(i%3==0) "은붕" else if(i%3==1)"가붕" else "윤똥",
                    date = if(i%3==0) "1999-01-06" else if(i%3==1) "2000-09-14" else "2002-06-08",
                    url = "www.google.com"
                )
            )
        }

         */
    }

    private fun fillNoticeURLs(response:ResponseBody) = viewModelScope.launch(Dispatchers.IO){
        // 로그인 후에 얻을 수 있는 url 들도 등록
        val document = Jsoup.parse(response.string())
        val majorNoticeList = document.getElementsByClass("m2")
        majorNoticeList.forEach {
            val connectedUrls = it.getElementsByAttribute("href").toString().split("//")
            with(connectedUrls.last()){
                val url = this.substring(0,this.indexOf('$'))
                noticeURLList[2] = "https://$url"
            }
        }
    }
}