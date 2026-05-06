package com.example.healthsync.data.samsung

import com.example.healthsync.domain.model.ExerciseSession
import com.example.healthsync.domain.model.HeartRateSample
import com.example.healthsync.domain.model.SleepSession
import com.example.healthsync.domain.model.StepSample
import javax.inject.Inject

/**
 * Converts raw SDK row maps to domain models.
 * Key names must match SDK HealthConstants for the data type.
 */
class SamsungHealthDataMapper @Inject constructor() {

    fun mapHeartRate(rows: List<Map<String, Any>>): List<HeartRateSample> =
        rows.map { row ->
            HeartRateSample(
                timestamp = row["startTime"] as Long,
                bpm = (row["heartRate"] as Number).toInt(),
            )
        }

    fun mapSteps(rows: List<Map<String, Any>>): List<StepSample> =
        rows.map { row ->
            StepSample(
                timestamp = row["startTime"] as Long,
                count = (row["count"] as Number).toInt(),
            )
        }

    fun mapSleep(rows: List<Map<String, Any>>): List<SleepSession> =
        rows.map { row ->
            SleepSession(
                timestamp = row["startTime"] as Long,
                startTime = row["startTime"] as Long,
                endTime = row["endTime"] as Long,
            )
        }

    fun mapExercise(rows: List<Map<String, Any>>): List<ExerciseSession> =
        rows.map { row ->
            ExerciseSession(
                timestamp = row["startTime"] as Long,
                startTime = row["startTime"] as Long,
                endTime = row["endTime"] as Long,
                exerciseType = row["exerciseType"]?.toString() ?: "unknown",
                calorie = (row["calorie"] as? Number)?.toDouble()?.takeIf { it > 0 },
                distance = (row["distance"] as? Number)?.toDouble()?.takeIf { it > 0 },
            )
        }
}
