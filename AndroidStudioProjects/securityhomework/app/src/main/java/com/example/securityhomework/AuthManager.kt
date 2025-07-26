package com.example.securityhomework

import android.content.Context
import androidx.core.content.edit

class AuthManager(context: Context) {

    private val sharedPrefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    private val testUser = User(
        email = "otus@test.com",
        password = "otus"
    )

    fun isTestUser(email: String, password: String): Boolean {
        return if (email == testUser.email && password == testUser.password) {
            sharedPrefs.edit { putString("LOGGED_USER", email) }
            true
        } else {
            false
        }
    }

    fun getCurrentUser(): String? {
        return sharedPrefs.getString("LOGGED_USER", null)
    }

    fun isBiometricEnabled(): Boolean {
        return sharedPrefs.getBoolean("biometric_enabled_${testUser.email}", false)
    }

    fun setBiometricEnabled(enabled: Boolean) {
        sharedPrefs.edit { putBoolean("biometric_enabled_${testUser.email}", enabled) }
    }

    fun logout() {
        sharedPrefs.edit { remove("LOGGED_USER") }
    }
}

data class User(
    val email: String,
    val password: String
)