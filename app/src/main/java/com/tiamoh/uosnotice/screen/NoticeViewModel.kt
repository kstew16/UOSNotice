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
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
    private val defaultType = 0


    init {
        Log.d("vm","init vm")
        _currentType = defaultType
        _filterText = ""
        //loadNotices()
        filterNoticeBy(_filterText)
        login("kstew16","9mokaraBcomes16!")
    }

    fun login(id:String,pw:String) = viewModelScope.launch(Dispatchers.IO){
        val handler = CoroutineExceptionHandler{_,throwable ->
            throwable.message?.let { Log.d("vm", it) }
        }
        withContext(Dispatchers.IO+handler){
            //val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)
            val formId = FormDataUtil.getBody("ssoId",id)
            val formPw = FormDataUtil.getBody("password",pw)
            val formLoginType = FormDataUtil.getBody("loginType","normal")

            val responce = noticeRepository.postAccountInfo(formId,formPw,formLoginType)
            responce
        }
    }

    fun loadNotices(){
        Log.d("vm","postNotice $_currentType type")
        _list.postValue(getListOfNotices(_currentType))
    }

    fun filterNoticeBy(searchText:String){
        Log.d("vm","filter by $searchText, typeNO $_currentType")
        _filterText = searchText
        val filteredList = ArrayList<Notice>()
        if(_filterText == "") filteredList.addAll(getListOfNotices(_currentType))
        else getListOfNotices(_currentType).forEach { notice->
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
    private fun getListOfNotices(typeNo:Int): List<Notice>{
        viewModelScope.launch(Dispatchers.IO){
            val body = noticeRepository.getNotices(typeNo)
            body
        }
        // Repository 에게 데이터를 요청하고, 응답받은 데이터를 가공하여 list 화하여 반환하는것이 주 목표
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
        return sampleList
    }
}