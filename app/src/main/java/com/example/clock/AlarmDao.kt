package com.example.clock

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): LiveData<List<BaoThuc>>

    @Insert
    fun insertAlarm(alarm: BaoThuc)

    @Update
    suspend fun updateAlarm(alarm: BaoThuc)

    @Delete
    suspend fun deleteAlarm(alarm: BaoThuc)
}