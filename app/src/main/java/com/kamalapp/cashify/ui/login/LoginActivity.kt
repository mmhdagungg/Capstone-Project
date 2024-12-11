package com.kamalapp.cashify.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kamalapp.cashify.MainActivity
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.response.LoginResponse
import com.kamalapp.cashify.data.response.ProfileResponse
import com.kamalapp.cashify.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var btnLogin: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.progressBar)
        btnLogin = findViewById(R.id.loginButton)
        emailEditText = findViewById(R.id.ed_login_email)
        passwordEditText = findViewById(R.id.edPassword)

        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        showLoading(true)

        val loginData = mapOf(
            "email" to email,
            "password" to password
        )

        val client = ApiConfig.instance.loginUser(loginData)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && !loginResponse.token.isNullOrEmpty()) {
                        val token = loginResponse.token

                        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("TOKEN", token)
                            putBoolean("IS_LOGGED_IN", true)
                            apply()
                        }

                        Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()

                        fetchUserProfile(token)
                    } else {
                        Toast.makeText(this@LoginActivity, "Login gagal: Token tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Kesalahan tidak diketahui"
                    Toast.makeText(this@LoginActivity, "Login gagal: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@LoginActivity, "Login gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun fetchUserProfile(token: String) {
        val client = ApiConfig.instance.getUserProfile("Bearer $token")
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val profileResponse = response.body()
                    if (profileResponse != null) {
                        val userName = profileResponse.user?.name
                        val userId = profileResponse.user?.id

                        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("USER_NAME", userName)
                            putInt("USER_ID", userId ?: 0)
                            apply()
                        }

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Gagal memuat profil pengguna", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Gagal memuat profil pengguna: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Gagal memuat profil pengguna: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !isLoading
    }
}
