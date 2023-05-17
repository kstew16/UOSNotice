package com.tiamoh.uosnotice.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiamoh.uosnotice.data.model.Notice
import com.tiamoh.uosnotice.data.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import javax.inject.Inject

// 세션이 확실하다는 전제 하에 실행되는 뷰모델 로직
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

    private val noticeURLList = Array(7){""}.apply {
        // 홈페이지공지
        this[1] = "https://www.uos.ac.kr/korNotice/list.do?list_id=FA1" // &pageIndex=#
        // 학사공지는 계정 정보가 필요하기때문에 로그인 후에 세팅, &pageIndex=#
        this[2] = ""
        // 장학공지
        this[3] = "https://scholarship.uos.ac.kr/scholarship/notice/notice/list.do?brdBbsseq=1" // &pageIndex=#
        this[4] = "https://uostory.uos.ac.kr/site/program/recruit/list?menuid=003003004002001&type=C" // &currentpage=#
        this[5] = "https://uostory.uos.ac.kr/site/reservation/lecture/lectureList?menuid=003004002001&reservegroupid=1&viewtype=L&rectype=L&thumbnail=N" // &currentpage=#
        this[6] = "https://uostory.uos.ac.kr/site/reservation/lecture/lectureList?menuid=003003002002&reservegroupid=1&viewtype=L&rectype=J&thumbnail=N" // &currentpage=#
    }
    // 필터되기 전 리스트
    private val loadedNotice = ArrayList<Notice>()



    init {
        Log.d("vm","init vm")
        _currentType = defaultType
        _filterText = ""
        //loadNotices()
        //filterNoticeBy(_filterText)
        //loginAndCrawl("kstew16","projectPassword16!")
    }


    fun filterNoticeBy(searchText:String)=viewModelScope.launch{
        withContext(Dispatchers.Default){
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
            filteredList.forEach { Log.d("vm",it.title) }
            _list.postValue(filteredList)
        }
    }

    fun selectNoticeType(typeNo:Int){
        loadedNotice.clear()
        _currentType = typeNo
        Log.d("vm","set TypeNo $_currentType type")
        viewModelScope.launch {
            getListOfNotices(typeNo,0)
        }
    }

    // Todo : 파싱해서 만들기 : 코루틴
    private suspend fun getListOfNotices(typeNo:Int,index:Int){
        // 인터넷 작업이 들어있다! suspend 처리 안 하면 응답받을떄까지 blocking!
        withContext(Dispatchers.IO){
            if(noticeURLList[2]=="") noticeRepository.getNoticePortlet().body()?.let { fillMajorNoticeUrl(it) }
            // Repository 에게 데이터를 요청하고, 응답받은 데이터를 가공하여 list 화하여 반환하는것이 주 목표
            when{
                (typeNo==0)-> {//모아보기
                }
                (typeNo in 1..3)->{//포탈공지
                    noticeRepository.getPortalNoticePage(noticeURLList[typeNo],index,typeNo).forEach {
                        loadedNotice.add(it)
                    }
                }
                (typeNo in 4..6)->{//UoStory

                }
                else->{//에러처리

                }
            }
        }
        withContext(Dispatchers.Default) {filterNoticeBy(_filterText)}
    }

    private fun fillMajorNoticeUrl(response:ResponseBody){
        // 학과 공지는 로그인 후에 로드 가능
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