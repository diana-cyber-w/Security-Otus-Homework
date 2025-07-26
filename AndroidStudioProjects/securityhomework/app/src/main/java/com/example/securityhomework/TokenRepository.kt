package com.example.securityhomework

import Keys
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

class TokenRepository(context: Context) {
    private val sharedPrefs = context.getSharedPreferences("SecurePrefs", MODE_PRIVATE)
    private val securityHelper = SecurityHelper(sharedPrefs)
    val keys = Keys(sharedPrefs, context)
    val key = keys.getAesSecretKey()

    fun saveToken(token: String) {
        val encrypted = securityHelper.encrypt(token, key)
        sharedPrefs.edit().apply {
            putString("AUTH_TOKEN", encrypted)
            apply()
        }
    }

    fun getToken(): String? {
        val encrypted = sharedPrefs.getString("AUTH_TOKEN", null)
        return encrypted?.let { securityHelper.decrypt(it, key) }
    }

    fun clearToken() {
        sharedPrefs.edit { remove("AUTH_TOKEN") }
        keys.removeKeys("AUTH_TOKEN_KEY")
    }
}