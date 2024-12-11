package com.kamalapp.cashify.data.response

import com.google.gson.annotations.SerializedName

data class InputResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("dateAdded")
	val dateAdded: String? = null
)

data class InputData(
	val labaKotor: String,
	val bayarGaji: String,
	val bayarAir: String,
	val biayaListrik: String,
	val biayaTransport: String,
	val biayaPromosi: String,
	val biayaPackaging: String,
	val biayaPajak: String
)
