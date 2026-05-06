package com.example.healthsync.domain.model

data class StepSample(
    override val timestamp: Long,
    val count: Int,
) : HealthMetric
