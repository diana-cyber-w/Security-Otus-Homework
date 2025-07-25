package com.example.securityhomework

import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

private const val AES_KEY_ALIAS = "AUTH_TOKEN_KEY"

class SecurityHelper(val sharedPrefs: SharedPreferences) {

    fun encrypt(data: String, key: Key): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)

        val iv = cipher.iv
        sharedPrefs.edit {
            putString("${AES_KEY_ALIAS}_iv", Base64.encodeToString(iv, Base64.NO_WRAP))
        }

        val encrypted = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    fun decrypt(encryptedData: String, key: Key): String {
        val iv = Base64.decode(sharedPrefs.getString("${AES_KEY_ALIAS}_iv", null), Base64.NO_WRAP)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)

        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        val decodedBytes = Base64.decode(encryptedData, Base64.NO_WRAP)
        val decoded = cipher.doFinal(decodedBytes)
        return String(decoded, Charsets.UTF_8)
    }
}