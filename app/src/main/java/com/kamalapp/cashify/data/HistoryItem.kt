package com.kamalapp.cashify.data

data class HistoryItem(
    val idProfile: Int,
    val idData: Int,
    val title: String,
    val iconRes: Int,
    val date: String,
    val labaKotor: Int,
    val bayarGaji: Int,
    val bayarAir: Int,
    val biayaListrik: Int,
    val biayaTransport: Int,
    val biayaPromosi: Int,
    val biayaPackaging: Int,
    val biayaPajak: Int
)
