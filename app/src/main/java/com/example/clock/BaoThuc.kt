package com.example.clock

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "alarms")
data class BaoThuc(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var day: Int,
    var month: Int,
    var year: Int,
    var hour: Int,
    var min: Int,
    var state: Boolean
)