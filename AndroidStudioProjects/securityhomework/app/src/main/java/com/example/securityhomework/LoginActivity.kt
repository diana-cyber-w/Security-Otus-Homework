package com.example.securityhomework

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var authManager: AuthManager
    private lateinit var biometricAuthHelper: BiometricAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authManager = AuthManager(applicationContext)
        biometricAuthHelper = BiometricAuthHelper(this)

        setupViews()
        checkBiometricAuth()
    }

    private fun setupViews() {
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val biometricSwitch = findViewById<CheckBox>(R.id.cbBiometric)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (authManager.isTestUser(email, password)) {
                authManager.setBiometricEnabled(biometricSwitch.isChecked)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Неверные учетные данные", Toast.LENGTH_SHORT).show()
            }
        }

        biometricSwitch.isChecked = authManager.isBiometricEnabled()
    }

    private fun checkBiometricAuth() {
        if (authManager.getCurrentUser() != null && authManager.isBiometricEnabled()) {
            biometricAuthHelper.authenticate(
                activity = this,
                onSuccess = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },
                onFailure = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                })
        }
    }
}