package com.tiamoh.uosnotice.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(activity:Activity){
    private val sharedPreferences:SharedPreferences
    init {
        sharedPreferences = activity.getSharedPreferences("uos_notice_prefs",Context.MODE_PRIVATE)
    }
    fun getString(key: String, defValue: String): String {
        return sharedPreferences.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        sharedPreferences.edit().putString(key, str).apply()
    }

    fun getSetting(settingName:String,defValue: Boolean):Boolean{
        return sharedPreferences.getBoolean(settingName,defValue)
    }

    fun setSetting(settingName:String,settingValue:Boolean){
        sharedPreferences.edit().putBoolean(settingName,settingValue).apply()
    }
}