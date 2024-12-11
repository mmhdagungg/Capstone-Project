package com.kamalapp.cashify

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        var token: String? = null
        var userId: Int = 0
        var userName: String? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        val sharedPref = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPref.getString("TOKEN", null)
        userId = sharedPref.getInt("USER_ID", 0)
        userName = sharedPref.getString("USER_NAME", null)
    }


}
