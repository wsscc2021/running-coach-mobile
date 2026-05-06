package com.example.healthsync.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthsync.data.local.dao.HeartRateDao
import com.example.healthsync.data.local.entity.HeartRateEntity

@Database(entities = [HeartRateEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun heartRateDao(): HeartRateDao
}
