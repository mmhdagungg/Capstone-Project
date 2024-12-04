package com.kamalapp.cashify.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kamalapp.cashify.MainActivity
import com.kamalapp.cashify.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.loginButton)

        btnLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}