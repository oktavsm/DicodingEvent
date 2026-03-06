package com.dicoding.dicodingevent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.utils.Events
import kotlinx.coroutines.launch

class EventListViewModel(private val repository: EventRepository) : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Events<String>>()
    val errorMessage: LiveData<Events<String>> = _errorMessage

    fun getEvents(active: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getEvents(active)
                _listEvents.value = response.listEvents?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = Events("Gagal memuat data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
