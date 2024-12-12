package com.kamalapp.cashify.ui.detailHistory

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kamalapp.cashify.R
import java.text.NumberFormat
import java.util.Locale

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var tvLaba: TextView
    private lateinit var tvGaji: TextView
    private lateinit var tvAir: TextView
    private lateinit var tvListrik: TextView
    private lateinit var tvTransport: TextView
    private lateinit var tvPromosi: TextView
    private lateinit var tvPackaging: TextView
    private lateinit var tvPajak: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvPrediksi: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inisialisasi TextViews
        tvLaba = findViewById(R.id.tvlaba)
        tvGaji = findViewById(R.id.tvGaji)
        tvAir = findViewById(R.id.tvAir)
        tvListrik = findViewById(R.id.tvListrik)
        tvTransport = findViewById(R.id.tvTransport)
        tvPromosi = findViewById(R.id.tvPromosi)
        tvPackaging = findViewById(R.id.tvPackaging)
        tvPajak = findViewById(R.id.tvPajak)
        tvDate = findViewById(R.id.tvdate)
        tvPrediksi = findViewById(R.id.tvPrediksi)

        // Ambil data dari Intent
        val labaKotor = intent.getIntExtra("labaKotor", 0)
        val bayarGaji = intent.getIntExtra("bayarGaji", 0)
        val bayarAir = intent.getIntExtra("bayarAir", 0)
        val biayaListrik = intent.getIntExtra("biayaListrik", 0)
        val biayaTransport = intent.getIntExtra("biayaTransport", 0)
        val biayaPromosi = intent.getIntExtra("biayaPromosi", 0)
        val biayaPackaging = intent.getIntExtra("biayaPackaging", 0)
        val biayaPajak = intent.getIntExtra("biayaPajak", 0)
        val date = intent.getStringExtra("date")

        // Format angka dengan awalan Rp
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        val netMargin = if (labaKotor > 0) {
            ((labaKotor - bayarGaji - bayarAir - biayaListrik - biayaTransport - biayaPromosi - biayaPackaging - biayaPajak).toDouble() / labaKotor * 100)
        } else {
            0.0
        }
        val netMarginFormatted = String.format("%.1f%%", netMargin)

        // Set data ke TextView dengan format Rp
        tvLaba.text = currencyFormatter.format(labaKotor)
        tvGaji.text = currencyFormatter.format(bayarGaji)
        tvAir.text = currencyFormatter.format(bayarAir)
        tvListrik.text = currencyFormatter.format(biayaListrik)
        tvTransport.text = currencyFormatter.format(biayaTransport)
        tvPromosi.text = currencyFormatter.format(biayaPromosi)
        tvPackaging.text = currencyFormatter.format(biayaPackaging)
        tvPajak.text = currencyFormatter.format(biayaPajak)
        tvDate.text = date
        tvPrediksi.text = "Net Margin kamu pada data ini mencapai $netMarginFormatted"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
