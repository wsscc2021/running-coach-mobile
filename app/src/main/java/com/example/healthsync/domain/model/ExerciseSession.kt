package com.example.healthsync.domain.model

data class ExerciseSession(
    override val timestamp: Long,
    val startTime: Long,
    val endTime: Long,
    val exerciseType: String,
    val calorie: Double? = null,
    val distance: Double? = null,
) : HealthMetric
