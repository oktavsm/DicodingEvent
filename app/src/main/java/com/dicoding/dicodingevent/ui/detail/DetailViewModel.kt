package com.dicoding.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.data.response.Event
import com.dicoding.dicodingevent.utils.Events
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {

    private val _eventDetail = MutableLiveData<Event>()
    val eventDetail: LiveData<Event> = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Events<String>>()
    val errorMessage: LiveData<Events<String>> = _errorMessage

    fun getDetailEvent(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getEventDetail(id)
                _eventDetail.value = response.event!!
            } catch (e: Exception) {
                _errorMessage.value = Events("Gagal memuat detail: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getFavoriteById(id: String): LiveData<FavoriteEvent?> =
        repository.getFavoriteById(id)

    fun toggleFavorite(event: Event, isFavorite: Boolean) {
        viewModelScope.launch {
            val id = event.id?.toString() ?: return@launch
            if (isFavorite) {
                repository.deleteFavoriteById(id)
            } else {
                repository.insertFavorite(
                    FavoriteEvent(
                        id = id,
                        name = event.name ?: "",
                        mediaCover = event.mediaCover,
                        imageLogo = event.imageLogo,
                        summary = event.summary,
                        category = event.category,
                        ownerName = event.ownerName,
                        beginTime = event.beginTime
                    )
                )
            }
        }
    }
}
