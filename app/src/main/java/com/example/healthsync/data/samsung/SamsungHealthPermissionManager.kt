package com.example.healthsync.data.samsung

import android.app.Activity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wraps Samsung Health permission requests.
 * Replace the stubs with SDK PermissionKey + HealthPermissionManager calls.
 * Stub behaviour: hasPermissions() returns false so the UI shows the grant screen;
 * requestPermissions() grants immediately so the full nav flow can be tested without the SDK.
 */
@Singleton
class SamsungHealthPermissionManager @Inject constructor() {

    private val requiredDataTypes = listOf("HeartRate", "StepCount", "Sleep", "Exercise")

    fun requestPermissions(activity: Activity, onResult: (allGranted: Boolean) -> Unit) {
        // TODO: build SDK PermissionKey set from requiredDataTypes and call
        //       HealthPermissionManager(store).requestPermissions(keys, activity).setResultListener { ... }
        onResult(true)
    }

    fun hasPermissions(): Boolean {
        // TODO: return HealthPermissionManager(store).isPermissionAcquired(requiredKeys)
        return false
    }
}
