package com.kamalapp.cashify.data.response

import com.google.gson.annotations.SerializedName

data class DetailHistoryResponse(

	@field:SerializedName("data")
	val data: DataDetail? = null
)

data class DataDetail(

	@field:SerializedName("dateTime")
	val dateTime: String? = null,

	@field:SerializedName("bayarGaji")
	val bayarGaji: String? = null,

	@field:SerializedName("labaKotor")
	val labaKotor: String? = null,

	@field:SerializedName("id_data")
	val idData: Int? = null,

	@field:SerializedName("biayaTransport")
	val biayaTransport: String? = null,

	@field:SerializedName("biayaPromosi")
	val biayaPromosi: String? = null,

	@field:SerializedName("bayarAir")
	val bayarAir: String? = null,

	@field:SerializedName("biayaListrik")
	val biayaListrik: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("biayaPajak")
	val biayaPajak: String? = null,

	@field:SerializedName("id_profile")
	val idProfile: Int? = null,

	@field:SerializedName("biayaPackaging")
	val biayaPackaging: String? = null
)
