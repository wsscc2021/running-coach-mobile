package com.example.healthsync.data

import com.example.healthsync.data.local.dao.HeartRateDao
import com.example.healthsync.data.local.entity.HeartRateEntity
import com.example.healthsync.data.remote.api.HealthApiService
import com.example.healthsync.data.remote.dto.HeartRateDto
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
    private val apiService: HealthApiService,
) : HealthDataRepository {

    override fun observeHeartRate(): Flow<List<HeartRateSample>> =
        heartRateDao.observeAll().map { it.map { e -> HeartRateSample(e.timestamp, e.bpm) } }

    override fun observeSteps(): Flow<List<StepSample>> = TODO("Add StepEntity + DAO")

    override fun observeSleep(): Flow<List<SleepSession>> = TODO("Add SleepEntity + DAO")

    override fun observeExerciseSessions(): Flow<List<ExerciseSession>> = TODO("Add ExerciseEntity + DAO")

    override suspend fun syncAll() {
        val now = System.currentTimeMillis()
        val dayAgo = now - 24 * 60 * 60 * 1_000L

        val samples = mapper.mapHeartRate(reader.readHeartRate(dayAgo, now))
        heartRateDao.insertAll(samples.map { HeartRateEntity(it.timestamp, it.bpm) })
        apiService.uploadHeartRate(samples.map { HeartRateDto(it.timestamp, it.bpm) })
    }
}
