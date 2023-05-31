package com.example.assignment6

import androidx.room.*

@Dao
interface LocationDao {
    @Query("SELECT * FROM location_table")
    fun getAll(): List<Location>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: Location)

    @Query("DELETE FROM location_table")
    suspend fun deleteAll()

}