package com.example.healthsync.data.samsung

import com.samsung.android.sdk.health.data.data.entries.ExerciseSession
import com.samsung.android.sdk.health.data.request.DataType
import com.samsung.android.sdk.health.data.request.DataTypes
import com.samsung.android.sdk.health.data.request.LocalTimeFilter
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SamsungHealthDataReader @Inject constructor(
    private val client: SamsungHealthClient,
) {
    private fun timeFilter(startTime: Long, endTime: Long): LocalTimeFilter {
        val zoneId = ZoneId.systemDefault()

        return LocalTimeFilter.of(
            Instant.ofEpochMilli(startTime).atZone(zoneId).toLocalDateTime(),
            Instant.ofEpochMilli(endTime).atZone(zoneId).toLocalDateTime(),
        )
    }

    suspend fun readHeartRate(startTime: Long, endTime: Long): List<Map<String, Any>> {
        val request = DataTypes.HEART_RATE.readDataRequestBuilder
            .setLocalTimeFilter(timeFilter(startTime, endTime))
            .build()

        return client.requireStore().readData(request).dataList.map { data ->
            mapOf(
                "startTime" to data.startTime.toEpochMilli(),
                "heartRate" to (data.getValue(DataType.HeartRateType.HEART_RATE) ?: 0f),
            )
        }
    }

    suspend fun readSteps(startTime: Long, endTime: Long): List<Map<String, Any>> {
        val request = DataType.StepsType.TOTAL.requestBuilder
            .setLocalTimeFilter(timeFilter(startTime, endTime))
            .build()

        val total = client.requireStore()
            .aggregateData(request)
            .dataList
            .mapNotNull { it.value as? Long }
            .sum()

        return listOf(
            mapOf(
                "startTime" to startTime,
                "endTime" to endTime,
                "count" to total,
            )
        )
    }

    suspend fun readSleep(startTime: Long, endTime: Long): List<Map<String, Any>> {
        val request = DataTypes.SLEEP.readDataRequestBuilder
            .setLocalTimeFilter(timeFilter(startTime, endTime))
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
            .setLocalTimeFilter(timeFilter(startTime, endTime))
            .build()

        return client.requireStore().readData(request).dataList.map { data ->
            val sessions = data.getValue(DataType.ExerciseType.SESSIONS)
                ?: emptyList<ExerciseSession>()

            val firstSession = sessions.firstOrNull()

            buildMap<String, Any> {
                put("startTime", data.startTime.toEpochMilli())
                put("endTime", (data.endTime ?: data.startTime).toEpochMilli())

                put(
                    "exerciseType",
                    data.getValue(DataType.ExerciseType.EXERCISE_TYPE)?.toString()
                        ?: firstSession?.exerciseType?.toString()
                        ?: "unknown"
                )

                firstSession?.let { session ->
                    put("calorie", session.calories.toDouble())

                    session.distance?.let { distance ->
                        put("distance", distance.toDouble())
                    }
                }
            }
        }
    }
}