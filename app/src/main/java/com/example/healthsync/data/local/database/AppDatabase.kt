package com.example.healthsync.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthsync.data.local.dao.ExerciseDao
import com.example.healthsync.data.local.dao.HeartRateDao
import com.example.healthsync.data.local.dao.SleepDao
import com.example.healthsync.data.local.dao.StepDao
import com.example.healthsync.data.local.entity.ExerciseEntity
import com.example.healthsync.data.local.entity.HeartRateEntity
import com.example.healthsync.data.local.entity.SleepEntity
import com.example.healthsync.data.local.entity.StepEntity

@Database(
    entities = [HeartRateEntity::class, StepEntity::class, SleepEntity::class, ExerciseEntity::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun heartRateDao(): HeartRateDao
    abstract fun stepDao(): StepDao
    abstract fun sleepDao(): SleepDao
    abstract fun exerciseDao(): ExerciseDao
}
