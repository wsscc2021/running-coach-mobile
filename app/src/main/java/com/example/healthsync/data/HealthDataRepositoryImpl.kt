package com.example.healthsync.data

import com.example.healthsync.data.local.dao.ExerciseDao
import com.example.healthsync.data.local.dao.HeartRateDao
import com.example.healthsync.data.local.dao.SleepDao
import com.example.healthsync.data.local.dao.StepDao
import com.example.healthsync.data.local.entity.ExerciseEntity
import com.example.healthsync.data.local.entity.HeartRateEntity
import com.example.healthsync.data.local.entity.SleepEntity
import com.example.healthsync.data.local.entity.StepEntity
import com.example.healthsync.data.remote.api.HealthApiService
import com.example.healthsync.data.remote.dto.ExerciseDto
import com.example.healthsync.data.remote.dto.HeartRateDto
import com.example.healthsync.data.remote.dto.SleepDto
import com.example.healthsync.data.remote.dto.StepDto
import com.example.healthsync.data.samsung.SamsungHealthDataMapper
import com.example.healthsync.data.samsung.SamsungHealthDataReader
import com.example.healthsync.domain.model.ExerciseSession
import com.example.healthsync.domain.model.HeartRateSample
import com.example.healthsync.domain.model.SleepSession
import com.example.healthsync.domain.model.StepSample
import com.example.healthsync.domain.repository.HealthDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthDataRepositoryImpl @Inject constructor(
    private val reader: SamsungHealthDataReader,
    private val mapper: SamsungHealthDataMapper,
    private val heartRateDao: HeartRateDao,
    private val stepDao: StepDao,
    private val sleepDao: SleepDao,
    private val exerciseDao: ExerciseDao,
    private val apiService: HealthApiService,
) : HealthDataRepository {

    override fun observeHeartRate(): Flow<List<HeartRateSample>> =
        heartRateDao.observeAll().map { it.map { e -> HeartRateSample(e.timestamp, e.bpm) } }

    override fun observeSteps(): Flow<List<StepSample>> =
        stepDao.observeAll().map { it.map { e -> StepSample(e.timestamp, e.count) } }

    override fun observeSleep(): Flow<List<SleepSession>> =
        sleepDao.observeAll().map { it.map { e -> SleepSession(e.timestamp, e.startTime, e.endTime) } }

    override fun observeExerciseSessions(): Flow<List<ExerciseSession>> =
        exerciseDao.observeAll().map { it.map { e ->
            ExerciseSession(e.timestamp, e.startTime, e.endTime, e.exerciseType, e.calorie, e.distance)
        } }

    override suspend fun syncAll() {
        val now = System.currentTimeMillis()
        val dayAgo = now - 24 * 60 * 60 * 1_000L

        val hrSamples = mapper.mapHeartRate(reader.readHeartRate(dayAgo, now))
        heartRateDao.insertAll(hrSamples.map { HeartRateEntity(it.timestamp, it.bpm) })
        if (hrSamples.isNotEmpty()) apiService.uploadHeartRate(hrSamples.map { HeartRateDto(it.timestamp, it.bpm) })

        val stepSamples = mapper.mapSteps(reader.readSteps(dayAgo, now))
        stepDao.insertAll(stepSamples.map { StepEntity(it.timestamp, it.count) })
        if (stepSamples.isNotEmpty()) apiService.uploadSteps(stepSamples.map { StepDto(it.timestamp, it.count) })

        val sleepSessions = mapper.mapSleep(reader.readSleep(dayAgo, now))
        sleepDao.insertAll(sleepSessions.map { SleepEntity(it.startTime, it.endTime, it.timestamp) })
        if (sleepSessions.isNotEmpty()) apiService.uploadSleep(sleepSessions.map { SleepDto(it.timestamp, it.startTime, it.endTime) })

        val exSessions = mapper.mapExercise(reader.readExercise(dayAgo, now))
        exerciseDao.insertAll(exSessions.map { ExerciseEntity(it.startTime, it.endTime, it.timestamp, it.exerciseType, it.calorie, it.distance) })
        if (exSessions.isNotEmpty()) apiService.uploadExercise(exSessions.map { ExerciseDto(it.timestamp, it.startTime, it.endTime, it.exerciseType, it.calorie, it.distance) })
    }
}
