package com.kamalapp.cashify.ui.register

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvHaveAccount = findViewById<TextView>(R.id.tvHaveAccount)

        val text = getString(R.string.already_have_an_account_log_in)
        tvHaveAccount.text = Html.fromHtml(text)

        nameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val name = s.toString().trim()
                if (!isValidName(name)) {
                    nameField.error = "Nama tidak boleh mengandung angka atau simbol"
                }
            }
        })

        emailField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                if (!isValidEmail(email)) {
                    emailField.error = "Masukkan email yang valid"
                }
            }
        })

        val passwordLayout = findViewById<TextInputLayout>(R.id.edPasswordLayout)
        val passwordField = findViewById<TextInputEditText>(R.id.edPassword)
        val colorStateList = ColorStateList.valueOf(resources.getColor(R.color.red, null))
        passwordLayout.setHelperTextColor(colorStateList)

        passwordField.addTextChangedListener(object : TextWatcher {
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



        btnRegister.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (nameField.error != null || emailField.error != null || passwordField.error != null) {
                Toast.makeText(this, "Periksa kembali input Anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(name, email, password)
        }

        tvHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidName(name: String): Boolean {
        return name.matches(Regex("^[a-zA-Z\\s]+\$")) && name.isNotEmpty()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
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
