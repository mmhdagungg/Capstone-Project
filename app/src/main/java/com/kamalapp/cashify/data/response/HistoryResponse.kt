package com.kamalapp.cashify.data.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<Data>? = null // Ubah dari Data? ke List<Data>?
)

data class Data(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("id_profile")
	val idProfile: Int? = null,

	@field:SerializedName("labaKotor")
	val labaKotor: Int? = null,

	@field:SerializedName("bayarGaji")
	val bayarGaji: Int? = null,

	@field:SerializedName("bayarAir")
	val bayarAir: Int? = null,

	@field:SerializedName("biayaListrik")
	val biayaListrik: Int? = null,

	@field:SerializedName("biayaTransport")
	val biayaTransport: Int? = null,

	@field:SerializedName("biayaPromosi")
	val biayaPromosi: Int? = null,

	@field:SerializedName("biayaPackaging")
	val biayaPackaging: Int? = null,

	@field:SerializedName("biayaPajak")
	val biayaPajak: Int? = null,

	@field:SerializedName("dateAdded")
	val dateAdded: String? = null
)
