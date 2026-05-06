package com.example.healthsync.data.samsung

import android.app.Activity
import com.samsung.android.sdk.health.data.permission.AccessType
import com.samsung.android.sdk.health.data.permission.HealthPermissionManager
import com.samsung.android.sdk.health.data.permission.Permission
import com.samsung.android.sdk.health.data.request.DataType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SamsungHealthPermissionManager @Inject constructor(
    private val client: SamsungHealthClient,
) {
    private val requiredPermissions = setOf(
        Permission.of(DataType.HealthDataTypes.HEART_RATE, AccessType.READ),
        Permission.of(DataType.HealthDataTypes.STEP_COUNT, AccessType.READ),
        Permission.of(DataType.HealthDataTypes.SLEEP_SESSION, AccessType.READ),
        Permission.of(DataType.HealthDataTypes.EXERCISE_SESSION, AccessType.READ),
    )

    fun requestPermissions(activity: Activity, onResult: (allGranted: Boolean) -> Unit) {
        HealthPermissionManager(client.requireStore())
            .requestPermissions(requiredPermissions, activity)
            .setResultListener { result ->
                onResult(result.resultMap.all { it.value })
            }
    }

    fun hasPermissions(): Boolean =
        HealthPermissionManager(client.requireStore())
            .isPermissionAcquired(requiredPermissions)
}
