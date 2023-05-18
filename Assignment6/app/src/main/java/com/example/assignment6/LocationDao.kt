package com.example.assignment6

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM location_table")
    fun getAll(): List<Location>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: Location)


}