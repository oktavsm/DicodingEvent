package com.dicoding.dicodingevent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import com.dicoding.dicodingevent.utils.Events
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel : ViewModel() {

    private val _listUpcoming = MutableLiveData<List<ListEventsItem>>()
    val listUpcoming: LiveData<List<ListEventsItem>> = _listUpcoming

    private val _listFinished = MutableLiveData<List<ListEventsItem>>()
    val listFinished: LiveData<List<ListEventsItem>> = _listFinished

    private val _listSearch = MutableLiveData<List<ListEventsItem>>()
    val listSearch: LiveData<List<ListEventsItem>> = _listSearch

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Events<String>>()
    val errorMessage: LiveData<Events<String>> = _errorMessage

    private fun setError(message: String) {
        _errorMessage.value = Events(message)
    }

    fun getEvents(active: Int) {
        _isLoading.value = true
        val client = if (active == 1) ApiConfig.getApiService().getActiveEvents(1)
        else ApiConfig.getApiService().getFinishedEvents(0)
        client.enqueue(object : Callback<EventResponse>{
            override fun onResponse(
                call: Call<EventResponse?>,
                response: Response<EventResponse?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents?.filterNotNull() ?: emptyList()
                    if (active == 1) _listUpcoming.value = events else _listFinished.value = events
                } else {
                    setError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse?>, t: Throwable) {
                _isLoading.value = false
                setError("Failure: ${t.message}")
            }
        })
    }

    fun searchEvents(query: String, activeStatus: Int = -1) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchEvents(query = query, active = activeStatus)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listSearch.value = response.body()?.listEvents?.filterNotNull() ?: emptyList()
                } else {
                    setError("Error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<EventResponse?>, t: Throwable) {
                _isLoading.value = false
                setError("Failure: ${t.message}")
            }
        })
    }
}