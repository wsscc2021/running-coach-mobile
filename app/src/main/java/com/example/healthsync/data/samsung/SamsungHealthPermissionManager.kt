package com.example.healthsync.data.samsung

import android.app.Activity
import com.samsung.android.sdk.healthdata.HealthConstants
import com.samsung.android.sdk.healthdata.HealthPermissionManager
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SamsungHealthPermissionManager @Inject constructor(
    private val client: SamsungHealthClient,
) {
    private val requiredPermissions = setOf(
        PermissionKey(HealthConstants.HeartRate.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ),
        PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ),
        PermissionKey(HealthConstants.Sleep.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ),
        PermissionKey(HealthConstants.Exercise.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ),
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
