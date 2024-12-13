package com.kamalapp.cashify.ui.splashScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kamalapp.cashify.MainActivity
import com.kamalapp.cashify.R
import com.kamalapp.cashify.ui.welcome.WelcomeActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val logoImageView = findViewById<ImageView>(R.id.logoImage)


        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val scaleUpAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        logoImageView.startAnimation(fadeInAnimation)
        logoImageView.startAnimation(scaleUpAnimation)


        scaleUpAnimation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                Handler().postDelayed({
                    navigateToNextScreen()
                }, 500)
            }
        })


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun navigateToNextScreen() {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("IS_LOGGED_IN", false)

        val intent = if (isLoggedIn) {

            Intent(this, MainActivity::class.java)
        } else {

            Intent(this, WelcomeActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
