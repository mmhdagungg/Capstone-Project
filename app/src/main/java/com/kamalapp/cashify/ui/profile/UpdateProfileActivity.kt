package com.kamalapp.cashify.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.retrofit.ApiConfig
import com.kamalapp.cashify.data.response.ProfileResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var edUpdateNama: TextInputEditText
    private lateinit var edUpdateNamaLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)


        edUpdateNama = findViewById(R.id.edUpdateNama)
        edUpdateNamaLayout = findViewById(R.id.edUpdateNamaLayout)

        val token = getToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show()
            return
        }

        val btnUpdateProfile = findViewById<Button>(R.id.btnUpdateProfile)
        btnUpdateProfile.setOnClickListener {
            val newName = edUpdateNama.text.toString().trim()

            if (newName.isNotEmpty()) {
                updateUserProfile(token, newName)
            } else {
                Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getToken(): String? {
        val sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPref.getString("TOKEN", null)
    }

    private fun updateUserProfile(token: String, newName: String) {
        val apiService = ApiConfig.instance
        val client = apiService.updateUserProfile(token, mapOf("name" to newName))

        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateProfileActivity, "Nama berhasil diperbarui", Toast.LENGTH_SHORT).show()

                    // Kirimkan hasil kembali ke ProfileActivity
                    setResult(RESULT_OK)
                    finish() // Kembali ke ProfileActivity setelah berhasil
                } else {
                    Toast.makeText(
                        this@UpdateProfileActivity,
                        "Gagal memperbarui nama: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@UpdateProfileActivity, "Kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
