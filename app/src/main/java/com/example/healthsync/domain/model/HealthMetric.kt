package com.example.healthsync.domain.model

sealed interface HealthMetric {
    val timestamp: Long
}
