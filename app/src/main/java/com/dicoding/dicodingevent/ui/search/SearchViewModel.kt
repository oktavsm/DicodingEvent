package com.dicoding.dicodingevent.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.utils.Events
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: EventRepository) : ViewModel() {

    private val _listSearch = MutableLiveData<List<ListEventsItem>>()
    val listSearch: LiveData<List<ListEventsItem>> = _listSearch

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Events<String>>()
    val errorMessage: LiveData<Events<String>> = _errorMessage

    fun searchEvents(query: String, active: Int = -1) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.searchEvents(query, active)
                _listSearch.value = response.listEvents?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = Events("Pencarian gagal: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
