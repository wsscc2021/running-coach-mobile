package com.example.healthsync.di

import android.content.Context
import androidx.room.Room
import com.example.healthsync.data.local.database.AppDatabase
import com.example.healthsync.data.remote.api.HealthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "health_sync.db").build()

    @Provides
    fun provideHeartRateDao(db: AppDatabase) = db.heartRateDao()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideHealthApiService(retrofit: Retrofit): HealthApiService =
        retrofit.create(HealthApiService::class.java)
}
