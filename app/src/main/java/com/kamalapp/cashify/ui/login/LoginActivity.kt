package com.kamalapp.cashify.ui.login

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.kamalapp.cashify.MainActivity
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.response.LoginResponse
import com.kamalapp.cashify.data.response.ProfileResponse
import com.kamalapp.cashify.data.retrofit.ApiConfig
import com.kamalapp.cashify.ui.register.RegisterActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var btnLogin: Button
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvRegister = findViewById(R.id.tvDontHaveAccount)
        val text = getString(R.string.don_t_have_an_account_yet_register)
        tvRegister.text = Html.fromHtml(text)

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        progressBar = findViewById(R.id.progressBar)
        btnLogin = findViewById(R.id.loginButton)
        emailLayout = findViewById(R.id.edEmailLayout)
        passwordLayout = findViewById(R.id.edPasswordLayout)
        emailEditText = findViewById(R.id.ed_login_email)
        passwordEditText = findViewById(R.id.edPassword)
        val colorStateList = ColorStateList.valueOf(resources.getColor(R.color.red, null))
        passwordLayout.setHelperTextColor(colorStateList)
        emailLayout.setHelperTextColor(colorStateList)

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                if (!isValidEmail(email)) {
                    emailLayout.helperText = "Silakan masukkan email yang valid"
                } else {
                    emailLayout.helperText = null
                }
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString().trim()
                if (!isValidPassword(password)) {
                    passwordLayout.helperText = "Password harus minimal 8 karakter"
                } else {
                    passwordLayout.helperText = null
                }
            }
        })

        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (!isValidEmail(email)) {
                emailLayout.helperText = "Silakan masukkan email yang valid"
                Toast.makeText(this, "Email tidak valid, silakan periksa kembali.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                emailLayout.helperText = null
            }

            if (!isValidPassword(password)) {
                passwordLayout.helperText = "Password harus minimal 8 karakter"
                Toast.makeText(this, "Password tidak valid, minimal 8 karakter.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                passwordLayout.helperText = null
            }

            loginUser(email, password)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
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
        val client = ApiConfig.instance.getUserProfile(token)
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
                            putString("TOKEN", token)
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
