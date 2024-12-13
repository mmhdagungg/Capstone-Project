package com.kamalapp.cashify.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kamalapp.cashify.R
import com.kamalapp.cashify.ui.login.LoginActivity
import com.kamalapp.cashify.ui.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        val btnMulai = findViewById<Button>(R.id.btnMulai)
        val tvHaveAccount = findViewById<TextView>(R.id.tvHaveAccount)
        val text = getString(R.string.already_have_an_account_log_in)
        tvHaveAccount.text = Html.fromHtml(text)

        btnMulai.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        tvHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))

        }
    }
}