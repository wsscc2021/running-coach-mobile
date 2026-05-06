package com.example.healthsync.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_sessions")
data class SleepEntity(
    @PrimaryKey val startTime: Long,
    val endTime: Long,
    val timestamp: Long,
)
