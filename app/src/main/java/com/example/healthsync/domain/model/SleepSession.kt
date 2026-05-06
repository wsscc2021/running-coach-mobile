package com.example.healthsync.domain.model

data class SleepSession(
    override val timestamp: Long,
    val startTime: Long,
    val endTime: Long,
    val stages: List<SleepStage> = emptyList(),
) : HealthMetric

data class SleepStage(
    val startTime: Long,
    val endTime: Long,
    val stage: SleepStageType,
)

enum class SleepStageType { AWAKE, LIGHT, DEEP, REM }
