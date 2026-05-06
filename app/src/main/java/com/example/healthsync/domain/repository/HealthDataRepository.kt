package com.example.healthsync.domain.repository

import com.example.healthsync.domain.model.ExerciseSession
import com.example.healthsync.domain.model.HeartRateSample
import com.example.healthsync.domain.model.SleepSession
import com.example.healthsync.domain.model.StepSample
import kotlinx.coroutines.flow.Flow

interface HealthDataRepository {
    fun observeHeartRate(): Flow<List<HeartRateSample>>
    fun observeSteps(): Flow<List<StepSample>>
    fun observeSleep(): Flow<List<SleepSession>>
    fun observeExerciseSessions(): Flow<List<ExerciseSession>>
    suspend fun syncAll()
}
