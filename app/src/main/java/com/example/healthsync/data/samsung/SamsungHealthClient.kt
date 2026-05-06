package com.example.healthsync.data.samsung

import android.content.Context
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult
import com.samsung.android.sdk.healthdata.HealthDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SamsungHealthClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    @Volatile private var store: HealthDataStore? = null

    fun connect(onConnected: () -> Unit, onError: (Exception) -> Unit) {
        if (store != null) {
            onConnected()
            return
        }

        val listener = object : HealthDataStore.ConnectionListener {
            override fun onConnected() = onConnected()
            override fun onConnectionFailed(error: HealthConnectionErrorResult) {
                store = null
                onError(Exception("Samsung Health connection failed (code: ${error.resultCode})"))
            }
            override fun onDisconnected() {
                store = null
            }
        }

        val newStore = HealthDataStore(context, listener)
        store = newStore
        newStore.connectService()
    }

    fun disconnect() {
        store?.disconnectService()
        store = null
    }

    fun requireStore(): HealthDataStore =
        checkNotNull(store) { "Samsung Health not connected — call connect() first" }
}
