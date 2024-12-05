package com.kamalapp.cashify.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kamalapp.cashify.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Set Toolbar as ActionBar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Disable default title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Enable Back Button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Handle Back Button Press
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
