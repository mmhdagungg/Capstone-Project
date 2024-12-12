package com.kamalapp.cashify.data.retrofit

import com.kamalapp.cashify.data.response.DashboardResponse
import com.kamalapp.cashify.data.response.HistoryResponse
import com.kamalapp.cashify.data.response.InputData
import com.kamalapp.cashify.data.response.InputResponse
import com.kamalapp.cashify.data.response.LoginResponse
import com.kamalapp.cashify.data.response.ProfileResponse
import com.kamalapp.cashify.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/api/users/register")
    fun registerUser(
        @Body user: Map<String, String>
    ): Call<RegisterResponse>

    @POST("/api/users/login")
    fun loginUser(
        @Body loginData: Map<String, String>
    ): Call<LoginResponse>

    @GET("/api/users/profile")
    fun getUserProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    // Update User Profile
    @PUT("/api/users/profile")
    fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body user: Map<String, String>
    ): Call<ProfileResponse>


    @GET("api/dashboard/{id_profile}/{dateTime}")
    fun getDashboardData(
        @Header("Authorization") token: String,
        @Path("id_profile") userId: Int,
        @Path("dateTime") dateTime: String
    ): Call<DashboardResponse>

    @POST("/api/data/{id_profile}/input")
    fun inputData(
        @Header("Authorization") token: String,
        @Path("id_profile") userId: Int,
        @Body inputData: InputData
    ): Call<InputResponse>

    @GET("/api/data/{id_profile}/history/{dateTime}")
    fun getHistoryData(
        @Header("Authorization") token: String,
        @Path("id_profile") userId: Int,
        @Path("dateTime") dateTime: String
    ): Call<HistoryResponse>



}