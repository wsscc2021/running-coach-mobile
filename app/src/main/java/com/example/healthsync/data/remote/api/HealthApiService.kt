package com.example.healthsync.data.remote.api

import com.example.healthsync.data.remote.dto.HeartRateDto
import retrofit2.http.Body
import retrofit2.http.POST

interface HealthApiService {
    @POST("health/heart-rate")
    suspend fun uploadHeartRate(@Body samples: List<HeartRateDto>)
}
