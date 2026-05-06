package com.example.healthsync.data.samsung

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the connection lifecycle to Samsung Health.
 * Adapt connect() using the SDK's connection API from developer.samsung.com/health.
 */
@Singleton
class SamsungHealthClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    // Replace Any with the SDK's store type (e.g. HealthDataStore)
    private var store: Any? = null

    val isConnected: Boolean get() = store != null

    fun connect(onConnected: () -> Unit, onError: (Exception) -> Unit) {
        // TODO: initialise SDK store and call connectService()
        // store = HealthDataStore(context, connectionListener)
        // store?.connectService()
        onConnected()
    }

    fun disconnect() {
        // TODO: store?.disconnectService()
        store = null
    }

    fun requireStore(): Any = checkNotNull(store) { "Samsung Health store not connected" }
}
