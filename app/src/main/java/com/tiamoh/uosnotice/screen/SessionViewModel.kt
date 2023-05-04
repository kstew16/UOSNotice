package com.tiamoh.uosnotice.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiamoh.uosnotice.data.repository.SessionRepository
import com.tiamoh.uosnotice.util.FormDataUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject


@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
    ): ViewModel(){
    private var _isPortalOnline = MutableLiveData<Boolean>()
    val isPortalOnline: LiveData<Boolean>
        get() = _isPortalOnline
    private var _isStoryOnline = MutableLiveData<Boolean>()
    val isStoryOnline: LiveData<Boolean>
        get() = _isStoryOnline

    private var lastLoginID = ""
    private var _failedLoginCount = MutableLiveData<Int>()

    init {
        _isPortalOnline.postValue(false)
        _isStoryOnline.postValue(false)
        _failedLoginCount.postValue(0)
    }

    fun putAccountInfo(id:String, pw:String) = viewModelScope.launch(Dispatchers.IO){
        // Todo: 여기도 로그 지우기
        Log.d("vm","login with $id $pw")
        if(id!=lastLoginID) _failedLoginCount.postValue(0)

        val handler = CoroutineExceptionHandler{_,throwable ->
            throwable.message?.let { Log.d("vm", it) }
        }
        _isPortalOnline.postValue(
            withContext(Dispatchers.IO+handler){
                val formId = FormDataUtil.getBody("ssoId",id)
                val formPw = FormDataUtil.getBody("password",pw)
                val formLoginType = FormDataUtil.getBody("loginType","normal")

                val loginResponse = sessionRepository.postAccountInfo(formId,formPw,formLoginType)
                if(loginResponse.headers().size!=7){
                    //_isLoggedIn.postValue(false)
                    Log.d("vm","login failed!")
                    _failedLoginCount.postValue(_failedLoginCount.value?.plus(1) ?: 1)
                    false
                }else{
                    //_isLoggedIn.postValue(true)
                    Log.d("vm","login suceed!")
                    _failedLoginCount.postValue(0)
                    true
                }
            }
        )
    }

    fun checkSession() = viewModelScope.launch(Dispatchers.IO) {
        val portalResponse  = withContext(Dispatchers.IO) {
            sessionRepository.checkPortalSession().body()
        }
        val storyResponse = withContext(Dispatchers.IO) {
            sessionRepository.checkStorySession().body()
        }
        Log.d("vm","got response")
        portalResponse?.let{
            val document = Jsoup.parse(it.string())
            val portlet = document.getElementsByClass("Portlet_tab_normal m1")
            if(portlet.size==0) _isPortalOnline.postValue(false)
            else _isPortalOnline.postValue(true)
        }
        storyResponse?.let{

            val document = Jsoup.parse(it.string())
            var loginNeed = document.title()!="UOStory 포트폴리오시스템"
            val links = document.getElementsByAttribute("href")
            for(i in links.indices){
                if(links[i].data()=="LOGIN"){
                    loginNeed = true
                    break
                }
            }
            _isStoryOnline.postValue(!loginNeed)
        }

    }


    companion object{
        const val OFFLINE = 0
        const val PORTAL_ONLINE = 1
        const val STORY_ONLINE = 2
        const val ONLINE_TOTALLY = 3
    }
}