package com.kamalapp.cashify

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kamalapp.cashify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var userId: Int = 0
    private var token: String? = null
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve saved shared preferences
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("USER_ID", 0)
        token = sharedPref.getString("TOKEN", null)
        userName = sharedPref.getString("USER_NAME", null)

        // Check if the user is logged in (token and userId should not be null or empty)
        if (token.isNullOrEmpty() || userId == 0) {
            Toast.makeText(this, "Data tidak ditemukan, silakan login kembali.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set up BottomNavigationView with NavController
        val navView: BottomNavigationView = binding.navView
        supportActionBar?.hide()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        // Optionally display the user data or do something with the values
        Toast.makeText(this, "User ID: $userId\nToken: $token", Toast.LENGTH_SHORT).show()
    }
}

