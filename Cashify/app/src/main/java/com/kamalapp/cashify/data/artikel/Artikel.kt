package com.kamalapp.cashify.data.artikel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artikel(
    val judul: String,
    val deskripsi: String,
    val gambar: Int,
    val kategori: String,
    val link: String
) : Parcelable
