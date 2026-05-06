package com.example.healthsync.data.samsung

import com.samsung.android.sdk.health.data.request.DataType
import com.samsung.android.sdk.health.data.request.DataTypes
import com.samsung.android.sdk.health.data.request.InstantTimeFilter
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SamsungHealthDataReader @Inject constructor(
    private val client: SamsungHealthClient,
) {
    private fun timeFilter(startTime: Long, endTime: Long) =
        InstantTimeFilter.of(Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime))

    suspend fun readHeartRate(startTime: Long, endTime: Long): List<Map<String, Any>> {
        val request = DataTypes.HEART_RATE.readDataRequestBuilder
            .setTimeFilter(timeFilter(startTime, endTime))
            .build()
        return client.requireStore().readData(request).dataList.map { data ->
            mapOf(
                "startTime" to data.startTime.toEpochMilli(),
                "heartRate" to (data.getValue(DataType.HeartRateType.HEART_RATE) ?: 0f),
            )
        }
    }

    suspend fun readSteps(startTime: Long, endTime: Long): List<Map<String, Any>> {
        val request = DataTypes.STEPS.readDataRequestBuilder
            .setTimeFilter(timeFilter(startTime, endTime))
            .build()
        return client.requireStore().readData(request).dataList.map { data ->
            mapOf(
                "startTime" to data.startTime.toEpochMilli(),
                "count" to (data.getValue(DataType.StepsType.STEPS) ?: 0),
            )
        }
    }

    suspend fun readSleep(startTime: Long, endTime: Long): List<Map<String, Any>> {
        val request = DataTypes.SLEEP.readDataRequestBuilder
            .setTimeFilter(timeFilter(startTime, endTime))
            .build()
        return client.requireStore().readData(request).dataList.map { data ->
            mapOf(
                "startTime" to data.startTime.toEpochMilli(),
                "endTime" to (data.endTime ?: data.startTime).toEpochMilli(),
            )
        }
    }

    suspend fun readExercise(startTime: Long, endTime: Long): List<Map<String, Any>> {
        val request = DataTypes.EXERCISE.readDataRequestBuilder
            .setTimeFilter(timeFilter(startTime, endTime))
            .build()
        return client.requireStore().readData(request).dataList.map { data ->
            buildMap {
                put("startTime", data.startTime.toEpochMilli())
                put("endTime", (data.endTime ?: data.startTime).toEpochMilli())
                put("exerciseType", data.getValue(DataType.ExerciseType.EXERCISE_TYPE)?.toString() ?: "unknown")
                val calories = data.getValue(DataType.ExerciseType.CALORIES)
                if (calories != null && calories > 0f) put("calorie", calories.toDouble())
                val distance = data.getValue(DataType.ExerciseType.DISTANCE)
                if (distance != null && distance > 0f) put("distance", distance.toDouble())
            }
        }
    }
}
