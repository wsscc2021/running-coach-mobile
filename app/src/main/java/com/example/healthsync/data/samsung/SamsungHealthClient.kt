package com.example.healthsync.data.samsung

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the connection lifecycle to Samsung Health.
 * Replace the stub body of connect() with the real SDK calls from developer.samsung.com/health.
 */
@Singleton
class SamsungHealthClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private enum class Status { DISCONNECTED, CONNECTED }

    private var status = Status.DISCONNECTED

    fun connect(onConnected: () -> Unit, onError: (Exception) -> Unit) {
        if (status == Status.CONNECTED) {
            onConnected()
            return
        }
        // TODO: replace with real SDK — HealthDataStore(context, connectionListener).connectService()
        status = Status.CONNECTED
        onConnected()
    }

    fun disconnect() {
        // TODO: store?.disconnectService()
        status = Status.DISCONNECTED
    }
}
