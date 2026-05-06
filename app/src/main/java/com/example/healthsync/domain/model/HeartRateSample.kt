package com.example.healthsync.domain.model

data class HeartRateSample(
    override val timestamp: Long,
    val bpm: Int,
) : HealthMetric
