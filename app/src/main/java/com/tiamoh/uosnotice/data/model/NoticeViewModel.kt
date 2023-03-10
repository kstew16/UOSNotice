package com.tiamoh.uosnotice.data.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoticeViewModel(defaultType:Int =0):ViewModel() {
    private var _list = MutableLiveData<List<NoticeItem>>()
    val list: LiveData<List<NoticeItem>>
        get() = _list
    private var _currentType:Int
    private var _filterText:String


    init {
        Log.d("vm","init vm")
        _currentType = defaultType
        _filterText = ""
        //loadNotices()
        filterNoticeBy(_filterText)
    }

    fun loadNotices(){
        Log.d("vm","postNotice $_currentType type")
        _list.postValue(getListOfNotices(_currentType))
    }

    fun filterNoticeBy(searchText:String){
        Log.d("vm","filter by $searchText, typeNO $_currentType")
        _filterText = searchText
        val filteredList = ArrayList<NoticeItem>()
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
    private fun getListOfNotices(typeNo:Int): List<NoticeItem>{
        Log.d("vm","getList $typeNo type")
        val sampleList = ArrayList<NoticeItem>()
        for(i in 1 until 1000){
            sampleList.add(
                NoticeItem(
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