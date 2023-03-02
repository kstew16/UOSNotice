package com.tiamoh.uosnotice.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoticeViewModel:ViewModel() {
    private var _list = MutableLiveData<List<NoticeItem>>()
    val list: LiveData<List<NoticeItem>>
        get() = _list

    init {
        loadNotices()
    }

    fun loadNotices(){
        _list.postValue(getListOfNotices())
    }

    fun filterNoticeBy(searchText:String){
        val filteredList = ArrayList<NoticeItem>()
        if(searchText == "") filteredList.addAll(getListOfNotices())
        else getListOfNotices().forEach { notice->
            if(notice.title.contains(searchText.lowercase())||
                        notice.date.contains(searchText.lowercase())||
                        notice.writer.contains(searchText.lowercase())
            ) filteredList.add(notice)
        }
        _list.postValue(filteredList)
    }

    // Todo : 파싱해서 만들기
    private fun getListOfNotices(): List<NoticeItem>{
        val sampleList = ArrayList<NoticeItem>()
        for(i in 1 until 1000){
            sampleList.add(
                NoticeItem(
                    title = "$i 번째 공지를 표시하는 중입니다.",
                    writer = if(i%3==0) "은붕" else if(i%3==1)"가붕" else "윤똥",
                    date = if(i%3==0) "1999-01-06" else if(i%3==1) "2000-09-14" else "2002-06-08",
                    url = "www.google.com"
                )
            )
        }
        return sampleList
    }
}