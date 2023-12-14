package com.example.clock

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BaoThuc::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}