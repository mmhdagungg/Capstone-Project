package com.kamalapp.cashify.data.response

import com.google.gson.annotations.SerializedName

data class InputResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("dateAdded")
	val dateAdded: String? = null
)

data class InputData(
	val labaKotor: Int,
	val bayarGaji: Int,
	val bayarAir: Int,
	val biayaListrik: Int,
	val biayaTransport: Int,
	val biayaPromosi: Int,
	val biayaPackaging: Int,
	val biayaPajak: Int
)
