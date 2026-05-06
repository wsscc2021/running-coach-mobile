package com.example.healthsync.data.remote.dto

data class ExerciseDto(
    val timestamp: Long,
    val startTime: Long,
    val endTime: Long,
    val exerciseType: String,
    val calorie: Double?,
    val distance: Double?,
)
