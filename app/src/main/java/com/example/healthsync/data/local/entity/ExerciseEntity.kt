package com.example.healthsync.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_sessions")
data class ExerciseEntity(
    @PrimaryKey val startTime: Long,
    val endTime: Long,
    val timestamp: Long,
    val exerciseType: String,
    val calorie: Double?,
    val distance: Double?,
)
