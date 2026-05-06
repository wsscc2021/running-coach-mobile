package com.example.healthsync.data.samsung

import com.samsung.android.sdk.health.data.request.DataType
import com.samsung.android.sdk.health.data.request.DataTypes
import com.samsung.android.sdk.health.data.request.InstantTimeFilter
import javax.inject.Inject
import javax.inject.Singleton
import java.time.Instant

@Singleton
class SamsungHealthDataReader @Inject constructor(
    private val client: SamsungHealthClient,
) {
    private fun instantTimeFilter(startTime: Long, endTime: Long) =
        InstantTimeFilter.of(
            Instant.ofEpochMilli(startTime),
            Instant.ofEpochMilli(endTime)
        )

    suspend fun readHeartRate(startTime: Long, endTime: Long): List<Map<String, Any>> {
        val request = DataTypes.HEART_RATE.readDataRequestBuilder
            .setInstantTimeFilter(instantTimeFilter(startTime, endTime))
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
            .setInstantTimeFilter(instantTimeFilter(startTime, endTime))
            .build()

        return client.requireStore().aggregateData(request).dataList.map { data ->
            mapOf(
                "startTime" to data.startTime.toEpochMilli(),
                "count" to (data.value ?: 0L),
            )
        }
    }

    suspend fun readSleep(startTime: Long, endTime: Long): List<Map<String, Any>> {
        val request = DataTypes.SLEEP.readDataRequestBuilder
            .setInstantTimeFilter(instantTimeFilter(startTime, endTime))
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
            .setInstantTimeFilter(instantTimeFilter(startTime, endTime))
            .build()

        return client.requireStore().readData(request).dataList.map { data ->
            buildMap<String, Any> {
                put("startTime", data.startTime.toEpochMilli())
                put("endTime", (data.endTime ?: data.startTime).toEpochMilli())
                put(
                    "exerciseType",
                    data.getValue(DataType.ExerciseType.EXERCISE_TYPE)?.toString() ?: "unknown"
                )

                val calories = data.getValue(DataType.ExerciseType.CALORIES)
                if (calories != null && calories > 0f) {
                    put("calorie", calories.toDouble())
                }

                val distance = data.getValue(DataType.ExerciseType.DISTANCE)
                if (distance != null && distance > 0f) {
                    put("distance", distance.toDouble())
                }
            }
        }
    }
}