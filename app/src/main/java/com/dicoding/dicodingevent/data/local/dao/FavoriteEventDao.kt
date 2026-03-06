package com.dicoding.dicodingevent.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent

@Dao
interface FavoriteEventDao {

    @Query("SELECT * FROM favorite_event ORDER BY name ASC")
    fun getAllFavorites(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_event WHERE id = :id")
    fun getFavoriteById(id: String): LiveData<FavoriteEvent?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(event: FavoriteEvent)

    @Delete
    suspend fun deleteFavorite(event: FavoriteEvent)

    @Query("DELETE FROM favorite_event WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)
}
