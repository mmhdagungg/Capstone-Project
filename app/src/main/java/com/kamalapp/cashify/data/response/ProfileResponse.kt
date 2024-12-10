package com.kamalapp.cashify.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("user")
	val user: UserProfile? = null
)

data class UserProfile(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
