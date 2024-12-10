package com.kamalapp.cashify.data.retrofit

import com.kamalapp.cashify.data.response.LoginResponse
import com.kamalapp.cashify.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/users/register")
    fun registerUser(
        @Body user: Map<String, String>
    ): Call<RegisterResponse>

    @POST("/api/users/login")
    fun loginUser(
        @Body loginData: Map<String, String>
    ): Call<LoginResponse>

}