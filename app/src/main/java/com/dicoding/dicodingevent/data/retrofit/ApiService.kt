package com.dicoding.dicodingevent.data.retrofit
import com.dicoding.dicodingevent.data.response.DetailEventResponse
import com.dicoding.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*
interface ApiService {

    //active event
    @GET("events")
    fun getActiveEvents(
        @Query("active") active: Int = 1
    ): Call<EventResponse>

    //finished
    @GET("events")
    fun getFinishedEvents(
        @Query("active") active: Int = 0
    ): Call<EventResponse>

    //searching
    @GET("events")
    fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String
    ): Call<EventResponse>

    //detail
    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: String
    ): Call<DetailEventResponse>
}