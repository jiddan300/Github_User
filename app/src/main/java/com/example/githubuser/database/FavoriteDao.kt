package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user:Favorite)

    @Delete
    fun delete(user:Favorite)

    @Query("Select * from favorite ORDER BY username ASC")
    fun getAllFavorite() : LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<Favorite>
}