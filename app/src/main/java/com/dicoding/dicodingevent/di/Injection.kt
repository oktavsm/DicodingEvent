package com.dicoding.dicodingevent.di

import android.content.Context
import com.dicoding.dicodingevent.data.local.database.EventDatabase
import com.dicoding.dicodingevent.data.remote.retrofit.ApiConfig
import com.dicoding.dicodingevent.data.repository.EventRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val db = EventDatabase.getInstance(context)
        val dao = db.favoriteEventDao()
        return EventRepository.getInstance(apiService, dao)
    }
}
