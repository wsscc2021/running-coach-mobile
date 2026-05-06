package com.example.healthsync.data.samsung

import android.app.Activity
import com.samsung.android.sdk.health.data.permission.AccessType
import com.samsung.android.sdk.health.data.permission.Permission
import com.samsung.android.sdk.health.data.request.DataTypes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SamsungHealthPermissionManager @Inject constructor(
    private val client: SamsungHealthClient,
) {
    private val requiredPermissions = setOf(
        Permission.of(DataTypes.HEART_RATE, AccessType.READ),
        Permission.of(DataTypes.STEPS, AccessType.READ),
        Permission.of(DataTypes.SLEEP, AccessType.READ),
        Permission.of(DataTypes.EXERCISE, AccessType.READ),
    )

    suspend fun requestPermissions(activity: Activity): Boolean {
        val granted = client.requireStore().requestPermissions(requiredPermissions, activity)
        return granted.containsAll(requiredPermissions)
    }

    suspend fun hasPermissions(): Boolean {
        val granted = client.requireStore().getGrantedPermissions(requiredPermissions)
        return granted.containsAll(requiredPermissions)
    }
}
