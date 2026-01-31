package com.dicoding.dicodingevent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.DetailEventResponse
import com.dicoding.dicodingevent.data.response.Event
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.dicoding.dicodingevent.utils.Events

class DetailViewModel : ViewModel() {

    private val _eventDetail = MutableLiveData<Event>()
    val eventDetail: LiveData<Event> = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Events<String>>()
    val errorMessage: LiveData<Events<String>> = _errorMessage

    private fun setError(message: String) {
        _errorMessage.value = Events(message)
    }
    fun getDetailEvent(id: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetail(id)
        client.enqueue(object : Callback<DetailEventResponse>{
            override fun onResponse(
                call: Call<DetailEventResponse?>,
                response: Response<DetailEventResponse?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _eventDetail.value = response.body()?.event as Event
                } else {
                    setError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailEventResponse?>, t: Throwable) {
                _isLoading.value = false
                setError("Gagal memuat data: ${t.message}")
            }
        })
    }


}