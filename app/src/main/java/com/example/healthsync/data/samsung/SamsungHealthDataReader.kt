package com.example.healthsync.data.samsung

import com.samsung.android.sdk.health.data.permission.AccessType
import com.samsung.android.sdk.health.data.request.DataType
import com.samsung.android.sdk.health.data.request.LocalTimeFilter
import com.samsung.android.sdk.health.data.request.ReadDataRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class SamsungHealthDataReader @Inject constructor(
    private val client: SamsungHealthClient,
) {

    suspend fun readHeartRate(startTime: Long, endTime: Long): List<Map<String, Any>> =
        suspendCancellableCoroutine { cont ->
            val request = ReadDataRequest.of(
                DataType.HealthDataTypes.HEART_RATE,
                LocalTimeFilter.of(Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime)),
            )
            client.requireStore()
                .readData(request)
                .setResultListener { result ->
                    try {
                        val rows = result.dataList.map { data ->
                            mapOf(
                                "startTime" to data.startTime.toEpochMilli(),
                                "heartRate" to data.heartRate,
                            )
                        }
                        if (cont.isActive) cont.resume(rows)
                    } catch (e: Exception) {
                        if (cont.isActive) cont.resumeWithException(e)
                    }
                }
        }

    suspend fun readSteps(startTime: Long, endTime: Long): List<Map<String, Any>> =
        suspendCancellableCoroutine { cont ->
            val request = ReadDataRequest.of(
                DataType.HealthDataTypes.STEP_COUNT,
                LocalTimeFilter.of(Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime)),
            )
            client.requireStore()
                .readData(request)
                .setResultListener { result ->
                    try {
                        val rows = result.dataList.map { data ->
                            mapOf(
                                "startTime" to data.startTime.toEpochMilli(),
                                "count" to data.count,
                            )
                        }
                        if (cont.isActive) cont.resume(rows)
                    } catch (e: Exception) {
                        if (cont.isActive) cont.resumeWithException(e)
                    }
                }
        }

    suspend fun readSleep(startTime: Long, endTime: Long): List<Map<String, Any>> =
        suspendCancellableCoroutine { cont ->
            val request = ReadDataRequest.of(
                DataType.HealthDataTypes.SLEEP_SESSION,
                LocalTimeFilter.of(Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime)),
            )
            client.requireStore()
                .readData(request)
                .setResultListener { result ->
                    try {
                        val rows = result.dataList.map { data ->
                            mapOf(
                                "startTime" to data.startTime.toEpochMilli(),
                                "endTime" to data.endTime.toEpochMilli(),
                            )
                        }
                        if (cont.isActive) cont.resume(rows)
                    } catch (e: Exception) {
                        if (cont.isActive) cont.resumeWithException(e)
                    }
                }
        }

    suspend fun readExercise(startTime: Long, endTime: Long): List<Map<String, Any>> =
        suspendCancellableCoroutine { cont ->
            val request = ReadDataRequest.of(
                DataType.HealthDataTypes.EXERCISE_SESSION,
                LocalTimeFilter.of(Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime)),
            )
            client.requireStore()
                .readData(request)
                .setResultListener { result ->
                    try {
                        val rows = result.dataList.map { data ->
                            buildMap {
                                put("startTime", data.startTime.toEpochMilli())
                                put("endTime", data.endTime.toEpochMilli())
                                put("exerciseType", data.exerciseType.toString())
                                val calorie = data.calorie
                                if (calorie != null && calorie > 0f) put("calorie", calorie.toDouble())
                                val distance = data.distance
                                if (distance != null && distance > 0f) put("distance", distance.toDouble())
                            }
                        }
                        if (cont.isActive) cont.resume(rows)
                    } catch (e: Exception) {
                        if (cont.isActive) cont.resumeWithException(e)
                    }
                }
        }
}
