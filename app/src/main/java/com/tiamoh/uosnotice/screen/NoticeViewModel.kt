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


    private var _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn
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
        _isLoggedIn.postValue(false)
        _filterText = ""
        _failedLoginCount.postValue(0)
        //loadNotices()
        //filterNoticeBy(_filterText)
        //loginAndCrawl("kstew16","projectPassword16!")

    }

    fun loginAndCrawl(id:String, pw:String) = viewModelScope.launch(Dispatchers.IO){
        // Todo: 여기도 로그 지우기
        Log.d("vm","login with $id $pw")
        if(id!=lastLoginID) _failedLoginCount.postValue(0)

        val handler = CoroutineExceptionHandler{_,throwable ->
            throwable.message?.let { Log.d("vm", it) }
        }
        _isLoggedIn.postValue(
            withContext(Dispatchers.IO+handler){
                val formId = FormDataUtil.getBody("ssoId",id)
                val formPw = FormDataUtil.getBody("password",pw)
                val formLoginType = FormDataUtil.getBody("loginType","normal")

                val loginResponse = noticeRepository.postAccountInfo(formId,formPw,formLoginType)
                if(loginResponse.headers().size!=7){
                    //_isLoggedIn.postValue(false)
                    Log.d("vm","login failed!")
                    _failedLoginCount.postValue(_failedLoginCount.value?.plus(1) ?: 1)
                    false
                }else{
                    //_isLoggedIn.postValue(true)
                    Log.d("vm","login suceed!")
                    _failedLoginCount.postValue(0)
                    // 파싱 로직
                    getListOfNotices(_currentType)
                    //val response = noticeRepository.getNoticePortlet()
                    //response.body()?.let { fillNoticeURLs(it) }
                    true
                }
            }
        )
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
        filteredList.forEach { Log.d("vm",it.title) }
        _list.postValue(filteredList)
    }


    fun selectNoticeType(typeNo:Int){
        _currentType = typeNo
        Log.d("vm","set TypeNo $_currentType type")
        filterNoticeBy(_filterText)
    }

    // Todo : 파싱해서 만들기 : 코루틴
    private fun getListOfNotices(typeNo:Int){
        Log.d("vm","getListOfNotices $_currentType")
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

            }
        }
        // Repository 에게 데이터를 요청하고, 응답받은 데이터를 가공하여 list 화하여 반환하는것이 주 목표

    }

    private fun fillNoticeURLs(response:ResponseBody) = viewModelScope.launch(Dispatchers.IO){
        Log.d("vm","fill Notice URLS")
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