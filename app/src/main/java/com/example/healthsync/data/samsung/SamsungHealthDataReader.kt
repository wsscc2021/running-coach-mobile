package com.example.healthsync.data.samsung

import android.os.Handler
import android.os.Looper
import com.samsung.android.sdk.healthdata.HealthConstants
import com.samsung.android.sdk.healthdata.HealthDataResolver
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class SamsungHealthDataReader @Inject constructor(
    private val client: SamsungHealthClient,
) {
    private val handler = Handler(Looper.getMainLooper())

    suspend fun readHeartRate(startTime: Long, endTime: Long): List<Map<String, Any>> =
        suspendCancellableCoroutine { cont ->
            val request = ReadRequest.Builder()
                .setDataType(HealthConstants.HeartRate.HEALTH_DATA_TYPE)
                .setLocalTimeRange(
                    HealthConstants.HeartRate.START_TIME,
                    HealthConstants.HeartRate.TIME_OFFSET,
                    startTime, endTime,
                )
                .build()

            HealthDataResolver(client.requireStore(), handler)
                .read(request)
                .setResultListener { result ->
                    try {
                        val rows = mutableListOf<Map<String, Any>>()
                        result.forEach { data ->
                            rows += mapOf(
                                "startTime" to data.getLong(HealthConstants.HeartRate.START_TIME),
                                "heartRate" to data.getInt(HealthConstants.HeartRate.HEART_RATE),
                            )
                        }
                        result.close()
                        if (cont.isActive) cont.resume(rows)
                    } catch (e: Exception) {
                        result.close()
                        if (cont.isActive) cont.resumeWithException(e)
                    }
                }
        }

    suspend fun readSteps(startTime: Long, endTime: Long): List<Map<String, Any>> =
        suspendCancellableCoroutine { cont ->
            val request = ReadRequest.Builder()
                .setDataType(HealthConstants.StepCount.HEALTH_DATA_TYPE)
                .setLocalTimeRange(
                    HealthConstants.StepCount.START_TIME,
                    HealthConstants.StepCount.TIME_OFFSET,
                    startTime, endTime,
                )
                .build()

            HealthDataResolver(client.requireStore(), handler)
                .read(request)
                .setResultListener { result ->
                    try {
                        val rows = mutableListOf<Map<String, Any>>()
                        result.forEach { data ->
                            rows += mapOf(
                                "startTime" to data.getLong(HealthConstants.StepCount.START_TIME),
                                "count" to data.getInt(HealthConstants.StepCount.COUNT),
                            )
                        }
                        result.close()
                        if (cont.isActive) cont.resume(rows)
                    } catch (e: Exception) {
                        result.close()
                        if (cont.isActive) cont.resumeWithException(e)
                    }
                }
        }

    suspend fun readSleep(startTime: Long, endTime: Long): List<Map<String, Any>> =
        suspendCancellableCoroutine { cont ->
            val request = ReadRequest.Builder()
                .setDataType(HealthConstants.Sleep.HEALTH_DATA_TYPE)
                .setLocalTimeRange(
                    HealthConstants.Sleep.START_TIME,
                    HealthConstants.Sleep.TIME_OFFSET,
                    startTime, endTime,
                )
                .build()

            HealthDataResolver(client.requireStore(), handler)
                .read(request)
                .setResultListener { result ->
                    try {
                        val rows = mutableListOf<Map<String, Any>>()
                        result.forEach { data ->
                            rows += mapOf(
                                "startTime" to data.getLong(HealthConstants.Sleep.START_TIME),
                                "endTime" to data.getLong(HealthConstants.Sleep.END_TIME),
                            )
                        }
                        result.close()
                        if (cont.isActive) cont.resume(rows)
                    } catch (e: Exception) {
                        result.close()
                        if (cont.isActive) cont.resumeWithException(e)
                    }
                }
        }

    suspend fun readExercise(startTime: Long, endTime: Long): List<Map<String, Any>> =
        suspendCancellableCoroutine { cont ->
            val request = ReadRequest.Builder()
                .setDataType(HealthConstants.Exercise.HEALTH_DATA_TYPE)
                .setLocalTimeRange(
                    HealthConstants.Exercise.START_TIME,
                    HealthConstants.Exercise.TIME_OFFSET,
                    startTime, endTime,
                )
                .build()

            HealthDataResolver(client.requireStore(), handler)
                .read(request)
                .setResultListener { result ->
                    try {
                        val rows = mutableListOf<Map<String, Any>>()
                        result.forEach { data ->
                            rows += buildMap {
                                put("startTime", data.getLong(HealthConstants.Exercise.START_TIME))
                                put("endTime", data.getLong(HealthConstants.Exercise.END_TIME))
                                put("exerciseType", data.getInt(HealthConstants.Exercise.EXERCISE_TYPE).toString())
                                val calorie = data.getFloat(HealthConstants.Exercise.CALORIE)
                                if (calorie > 0f) put("calorie", calorie.toDouble())
                                val distance = data.getFloat(HealthConstants.Exercise.DISTANCE)
                                if (distance > 0f) put("distance", distance.toDouble())
                            }
                        }
                        result.close()
                        if (cont.isActive) cont.resume(rows)
                    } catch (e: Exception) {
                        result.close()
                        if (cont.isActive) cont.resumeWithException(e)
                    }
                }
        }
}
