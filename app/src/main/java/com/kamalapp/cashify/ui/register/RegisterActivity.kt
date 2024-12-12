package com.kamalapp.cashify.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.response.RegisterResponse
import com.kamalapp.cashify.data.retrofit.ApiConfig
import com.kamalapp.cashify.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameField = findViewById<TextInputEditText>(R.id.edName)
        val emailField = findViewById<TextInputEditText>(R.id.edEmail)
        val passwordField = findViewById<TextInputEditText>(R.id.edPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvHaveAccount = findViewById<TextView>(R.id.tvHaveAccount)

        val text = getString(R.string.already_have_an_account_log_in)
        tvHaveAccount.text = Html.fromHtml(text)

        btnRegister.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(name, email, password)
        }


        tvHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }



    private fun registerUser(name: String, email: String, password: String) {
        val userData = mapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )

        // API call
        ApiConfig.instance.registerUser(userData)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration failed: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
