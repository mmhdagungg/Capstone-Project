package com.kamalapp.cashify.data.response

import com.google.gson.annotations.SerializedName

data class DashboardResponse(

	@field:SerializedName("revenue")
	val revenue: Int? = null,

	@field:SerializedName("net_balance")
	val netBalance: Int? = null,

	@field:SerializedName("net_margin")
	val netMargin: Int? = null,

	@field:SerializedName("hasil_prediksi")
	val hasilPrediksi: String? = null,

	@field:SerializedName("expenses")
	val expenses: Int? = null
)
