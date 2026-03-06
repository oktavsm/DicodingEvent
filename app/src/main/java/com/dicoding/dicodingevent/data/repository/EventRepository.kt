package com.dicoding.dicodingevent.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.dicodingevent.data.local.dao.FavoriteEventDao
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent
import com.dicoding.dicodingevent.data.remote.retrofit.ApiService
import com.dicoding.dicodingevent.data.response.DetailEventResponse
import com.dicoding.dicodingevent.data.response.EventResponse

class EventRepository private constructor(
    private val apiService: ApiService,
    private val favoriteEventDao: FavoriteEventDao
) {

    // Remote

    suspend fun getEvents(active: Int): EventResponse =
        apiService.getEvents(active)

    suspend fun searchEvents(query: String, active: Int = -1): EventResponse =
        apiService.searchEvents(active = active, query = query)

    suspend fun getEventDetail(id: String): DetailEventResponse =
        apiService.getEventDetail(id)

    suspend fun getActiveEventForReminder(): EventResponse =
        apiService.getActiveEventForReminder()

    // Local

    fun getAllFavorites(): LiveData<List<FavoriteEvent>> =
        favoriteEventDao.getAllFavorites()

    fun getFavoriteById(id: String): LiveData<FavoriteEvent?> =
        favoriteEventDao.getFavoriteById(id)

    suspend fun insertFavorite(event: FavoriteEvent) =
        favoriteEventDao.insertFavorite(event)

    suspend fun deleteFavoriteById(id: String) =
        favoriteEventDao.deleteFavoriteById(id)

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(
            apiService: ApiService,
            favoriteEventDao: FavoriteEventDao
        ): EventRepository {
            return INSTANCE ?: synchronized(this) {
                EventRepository(apiService, favoriteEventDao).also { INSTANCE = it }
            }
        }
    }
}
