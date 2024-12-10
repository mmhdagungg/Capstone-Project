package com.kamalapp.cashify.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.response.ProfileResponse
import com.kamalapp.cashify.data.retrofit.ApiConfig
import com.kamalapp.cashify.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvProfileName: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnUpdateProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvProfileName = findViewById(R.id.tvProfileName)
        btnLogout = findViewById(R.id.btnLogout)
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)

        // Periksa token saat aktivitas dimulai
        val token = getToken()
        if (token.isNullOrEmpty()) {
            redirectToLogin()
            return
        }

        // Panggil fungsi fetchUserProfile untuk mengambil nama user
        fetchUserProfile(token)

        // Button Update Profile
        btnUpdateProfile.setOnClickListener {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            startActivityForResult(intent, REQUEST_UPDATE_PROFILE)
        }

        // Logout
        btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getToken(): String? {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPref.getString("TOKEN", null)
    }

    private fun redirectToLogin() {
        Toast.makeText(this, "Token tidak ditemukan. Silakan login.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun fetchUserProfile(token: String) {
        val apiService = ApiConfig.instance
        val client = apiService.getUserProfile(token)

        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val profile = response.body()?.user
                    if (profile != null) {
                        tvProfileName.text = profile.name ?: "Nama Tidak Ditemukan"
                        println("Profile Name: ${profile.name}")
                    } else {
                        Toast.makeText(this@ProfileActivity, "Profil tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    println("Error Response: ${response.message()} - ${response.code()}")
                    println("Error Body: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@ProfileActivity,
                        "Gagal memuat data profil: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                println("Failure: ${t.message}")
                Toast.makeText(this@ProfileActivity, "Kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk menangani hasil dari UpdateProfileActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_UPDATE_PROFILE && resultCode == RESULT_OK) {
            // Jika update berhasil, panggil ulang fetchUserProfile untuk mendapatkan data terbaru
            val token = getToken()
            if (!token.isNullOrEmpty()) {
                fetchUserProfile(token)
            }
        }
    }

    private fun logoutUser() {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }

        Toast.makeText(this, "Logout berhasil!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        private const val REQUEST_UPDATE_PROFILE = 1001
    }
}
