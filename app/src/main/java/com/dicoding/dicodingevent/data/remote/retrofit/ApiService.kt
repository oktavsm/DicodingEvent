package com.dicoding.dicodingevent.data.remote.retrofit

import com.dicoding.dicodingevent.data.response.DetailEventResponse
import com.dicoding.dicodingevent.data.response.EventResponse
import retrofit2.http.*

interface ApiService {

    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int
    ): EventResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String
    ): EventResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: String
    ): DetailEventResponse

    // Daily reminder
    @GET("events")
    suspend fun getActiveEventForReminder(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 1
    ): EventResponse
}
