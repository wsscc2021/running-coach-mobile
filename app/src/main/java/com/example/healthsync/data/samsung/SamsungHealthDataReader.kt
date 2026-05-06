package com.example.healthsync.data.samsung

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads raw data from Samsung Health via the SDK resolver.
 * Replace TODO bodies with SDK ReadRequest/HealthDataResolver calls.
 */
@Singleton
class SamsungHealthDataReader @Inject constructor(
    private val client: SamsungHealthClient,
) {
    suspend fun readHeartRate(startTime: Long, endTime: Long): List<Map<String, Any>> =
        readRange("HeartRate", startTime, endTime)

    suspend fun readSteps(startTime: Long, endTime: Long): List<Map<String, Any>> =
        readRange("StepCount", startTime, endTime)

    suspend fun readSleep(startTime: Long, endTime: Long): List<Map<String, Any>> =
        readRange("Sleep", startTime, endTime)

    suspend fun readExercise(startTime: Long, endTime: Long): List<Map<String, Any>> =
        readRange("Exercise", startTime, endTime)

    private suspend fun readRange(
        dataType: String,
        startTime: Long,
        endTime: Long,
    ): List<Map<String, Any>> {
        // TODO: build ReadRequest, call resolver.read(), suspend until result arrives,
        //       convert each HealthData row to a Map and return.
        return emptyList()
    }
}
