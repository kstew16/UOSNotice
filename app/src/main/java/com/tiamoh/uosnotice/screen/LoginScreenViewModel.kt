package com.tiamoh.uosnotice.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginScreenViewModel: ViewModel() {
    private var _screenTitle = MutableLiveData("")
    val screenTitle: LiveData<String>
        get() = _screenTitle
    fun setTitle(newTitle:String){
        _screenTitle.value = newTitle
    }
}