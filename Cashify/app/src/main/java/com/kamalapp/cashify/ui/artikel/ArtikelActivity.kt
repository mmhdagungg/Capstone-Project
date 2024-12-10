package com.kamalapp.cashify.ui.artikel

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.kamalapp.cashify.databinding.ActivityArtikelBinding

class ArtikelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtikelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebView()
    }

    private fun setupWebView() {
        val url = intent.getStringExtra("URL") ?: "https://google.com"
        with(binding.webView) {
            webViewClient = WebViewClient()
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            loadUrl(url)
        }
    }
}
