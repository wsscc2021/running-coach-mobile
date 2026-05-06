package com.example.healthsync.data.samsung

import android.app.Activity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wraps Samsung Health permission requests.
 * Replace the TODO stubs with SDK permission API calls from developer.samsung.com/health.
 */
@Singleton
class SamsungHealthPermissionManager @Inject constructor(
    private val client: SamsungHealthClient,
) {
    // Data types to request; map to SDK PermissionKey constants
    private val requiredDataTypes = listOf(
        "HeartRate", "StepCount", "Sleep", "Exercise",
    )

    fun requestPermissions(activity: Activity, onResult: (allGranted: Boolean) -> Unit) {
        // TODO: build SDK PermissionKey set from requiredDataTypes, call requestPermissions()
        onResult(false)
    }

    fun hasPermissions(): Boolean {
        // TODO: call SDK isPermissionAcquired()
        return false
    }
}
