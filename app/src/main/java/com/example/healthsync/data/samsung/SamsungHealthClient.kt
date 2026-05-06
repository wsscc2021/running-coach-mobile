package com.example.healthsync.data.samsung

import android.content.Context
import com.samsung.android.sdk.health.data.HealthDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SamsungHealthClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val store: HealthDataStore by lazy {
        HealthDataStore.getStore(context)
    }

    fun requireStore(): HealthDataStore = store
}
