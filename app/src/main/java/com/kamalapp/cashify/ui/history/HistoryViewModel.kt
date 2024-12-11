package com.kamalapp.cashify.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kamalapp.cashify.data.response.HistoryResponse
import com.kamalapp.cashify.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {

    private val _historyData = MutableLiveData<HistoryResponse>()
    val historyData: LiveData<HistoryResponse> get() = _historyData

    private val apiService = ApiConfig.instance

    fun getHistory(token: String, userId: Int, dateTime: String): LiveData<HistoryResponse> {
        apiService.getHistoryData(token, userId, dateTime).enqueue(object :
            Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful) {
                    Log.d("HistoryViewModel", "Response: ${response.body()}")
                    _historyData.value = response.body()
                } else {
                    Log.e("HistoryViewModel", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Log.e("HistoryViewModel", "Failure: ${t.message}")
            }

        })
        return historyData
    }

}
