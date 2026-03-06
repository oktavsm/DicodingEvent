package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.utils.Events
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    private val _listUpcoming = MutableLiveData<List<ListEventsItem>>()
    val listUpcoming: LiveData<List<ListEventsItem>> = _listUpcoming

    private val _listFinished = MutableLiveData<List<ListEventsItem>>()
    val listFinished: LiveData<List<ListEventsItem>> = _listFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Events<String>>()
    val errorMessage: LiveData<Events<String>> = _errorMessage

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val upcoming = repository.getEvents(1)
                _listUpcoming.value = upcoming.listEvents?.filterNotNull()?.take(5) ?: emptyList()

                val finished = repository.getEvents(0)
                _listFinished.value = finished.listEvents?.filterNotNull()?.take(5) ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = Events("Gagal memuat data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
