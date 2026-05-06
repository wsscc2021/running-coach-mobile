package com.example.healthsync.ui.health

import android.app.Activity
import com.example.healthsync.data.samsung.SamsungHealthClient
import com.example.healthsync.data.samsung.SamsungHealthPermissionManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val client: SamsungHealthClient,
    private val permissionManager: SamsungHealthPermissionManager,
) : ViewModel() {

    sealed interface State {
        object Connecting : State
        object NeedsPermission : State
        object Granted : State
        data class Error(val message: String) : State
    }

    private val _state = MutableStateFlow<State>(State.Connecting)
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        connect()
    }

    fun connect() {
        _state.value = State.Connecting
        client.connect(
            onConnected = {
                _state.value = if (permissionManager.hasPermissions()) State.Granted else State.NeedsPermission
            },
            onError = { e ->
                _state.value = State.Error(e.message ?: "Failed to connect to Samsung Health")
            },
        )
    }

    fun requestPermissions(activity: Activity) {
        permissionManager.requestPermissions(activity) { granted ->
            _state.value = if (granted) State.Granted else State.NeedsPermission
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.disconnect()
    }
}
