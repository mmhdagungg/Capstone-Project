package com.kamalapp.cashify

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        token = MyApplication.token
        userId = MyApplication.userId
        userName = MyApplication.userName

        if (token.isNullOrEmpty() || userId == 0) {
            Toast.makeText(this, "Data tidak ditemukan, silakan login kembali.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val navView: BottomNavigationView = binding.navView
        supportActionBar?.hide()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        Toast.makeText(this, "User ID: $userId\nToken: $token", Toast.LENGTH_SHORT).show()
    }
}
