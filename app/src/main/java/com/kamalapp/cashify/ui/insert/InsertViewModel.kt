package com.kamalapp.cashify.ui.insert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kamalapp.cashify.data.response.InputData
import com.kamalapp.cashify.data.response.InputResponse
import com.kamalapp.cashify.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertViewModel : ViewModel() {

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> get() = _responseMessage

    fun inputData(token: String, userId: Int, inputData: InputData) {
        val client = ApiConfig.instance.inputData(token, userId, inputData)
        client.enqueue(object : Callback<InputResponse> {
            override fun onResponse(call: Call<InputResponse>, response: Response<InputResponse>) {
                if (response.isSuccessful) {
                    _responseMessage.value = response.body()?.message ?: "Data berhasil ditambahkan"
                } else {
                    val statusCode = response.code()
                    _responseMessage.value = "Gagal menambahkan data. Status Code: $statusCode"
                }

            }

            override fun onFailure(call: Call<InputResponse>, t: Throwable) {
                _responseMessage.value = "Error: ${t.message}"
            }
        })
    }

}
