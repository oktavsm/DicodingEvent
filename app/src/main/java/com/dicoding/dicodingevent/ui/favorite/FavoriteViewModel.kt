package com.dicoding.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.repository.EventRepository

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {
    val favorites = repository.getAllFavorites()
}
