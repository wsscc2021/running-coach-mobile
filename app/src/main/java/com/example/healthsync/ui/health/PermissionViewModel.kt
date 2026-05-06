package com.example.healthsync.ui.health

import android.app.Activity
import com.example.healthsync.data.samsung.SamsungHealthPermissionManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
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
        checkPermissions()
    }

    fun connect() {
        checkPermissions()
    }

    fun checkPermissions() {
        _state.value = State.Connecting
        viewModelScope.launch {
            try {
                _state.value =
                    if (permissionManager.hasPermissions()) State.Granted
                    else State.NeedsPermission
            } catch (e: Exception) {
                _state.value = State.Error(e.message ?: "Failed to connect to Samsung Health")
            }
        }
    }

    fun requestPermissions(activity: Activity) {
        viewModelScope.launch {
            try {
                val granted = permissionManager.requestPermissions(activity)
                _state.value = if (granted) State.Granted else State.NeedsPermission
            } catch (e: Exception) {
                _state.value = State.Error(e.message ?: "Permission request failed")
            }
        }
    }
}
