package com.tiamoh.uosnotice.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiamoh.uosnotice.data.repository.SessionRepository
import com.tiamoh.uosnotice.util.FormDataUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.ResponseBody
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
        // 이 스레드는 로그인 답 올 때 까지 기다렸다가 로그인 정보를 바꿔줘야 메인 UI가 반응함. 따라서 suspend 안 넣.
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
        // 네트워크 작업 둘은 병렬적으로 진행해야 좋다. async 로 진행하게 해 준다.
        val deferredIsPortalOnline  = async { getPortalResponse(sessionRepository) }
        val deferredIsStoryOnline = async { getStoryResponse(sessionRepository) }
        Log.d("vm","checking session")
        deferredIsStoryOnline.await().let{ _isStoryOnline.postValue(!it) }
        deferredIsPortalOnline.await().let{ _isPortalOnline.postValue(it) }
    }
    private suspend fun getPortalResponse(sessionRepository: SessionRepository):Boolean = withContext(Dispatchers.IO){
        sessionRepository.checkPortalSession().body()?.let {
            val document = Jsoup.parse(it.string())
            val portlet = document.getElementsByClass("Portlet_tab_normal m1")
            Log.d("vm","got response from portal")
            if(portlet.size!=0) return@withContext true
        }
        Log.d("vm","portal don't response")
        return@withContext false
    }
    private suspend fun getStoryResponse(sessionRepository: SessionRepository):Boolean = withContext(Dispatchers.IO){
        sessionRepository.checkStorySession().body()?.let {
            val document = Jsoup.parse(it.string())
            var loginNeed = document.title()!="UOStory 포트폴리오시스템"
            val links = document.getElementsByAttribute("href")
            for(i in links.indices){
                if(links[i].data()=="LOGIN"){
                    loginNeed = true
                    break
                }
            }
            Log.d("vm","got response from story")
            return@withContext loginNeed
        }
        Log.d("vm","portal don't response")
        return@withContext false
    }


    companion object{
        const val OFFLINE = 0
        const val PORTAL_ONLINE = 1
        const val STORY_ONLINE = 2
        const val ONLINE_TOTALLY = 3
    }
}