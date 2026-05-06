package com.example.healthsync.data.remote.api

import com.example.healthsync.data.remote.dto.ExerciseDto
import com.example.healthsync.data.remote.dto.HeartRateDto
import com.example.healthsync.data.remote.dto.SleepDto
import com.example.healthsync.data.remote.dto.StepDto
import retrofit2.http.Body
import retrofit2.http.POST

interface HealthApiService {
    @POST("health/heart-rate")
    suspend fun uploadHeartRate(@Body samples: List<HeartRateDto>)

    @POST("health/steps")
    suspend fun uploadSteps(@Body samples: List<StepDto>)

    @POST("health/sleep")
    suspend fun uploadSleep(@Body sessions: List<SleepDto>)

    @POST("health/exercise")
    suspend fun uploadExercise(@Body sessions: List<ExerciseDto>)
}
