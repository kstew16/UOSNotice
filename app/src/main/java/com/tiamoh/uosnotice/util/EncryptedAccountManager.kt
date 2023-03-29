package com.tiamoh.uosnotice.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.tiamoh.uosnotice.data.api.dto.AccountDTO

class EncryptedAccountManager(applicationContext:Context) {
    private val sharedPreferences:SharedPreferences

    init {
        val masterKey = MasterKey
            .Builder(applicationContext,MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        sharedPreferences = EncryptedSharedPreferences.create(
            applicationContext,
            "encrypted_account",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveAccount(accountDTO: AccountDTO){
        sharedPreferences.edit().putString("id", accountDTO.id).putString("pw",accountDTO.pw).apply()
    }
    fun getAccount():AccountDTO{
        return AccountDTO(sharedPreferences.getString("id","")!!,sharedPreferences.getString("pw","")!!)
    }
}