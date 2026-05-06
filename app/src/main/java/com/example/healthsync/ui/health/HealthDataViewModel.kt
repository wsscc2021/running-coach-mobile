package com.example.healthsync.ui.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthsync.domain.model.ExerciseSession
import com.example.healthsync.domain.model.HeartRateSample
import com.example.healthsync.domain.model.SleepSession
import com.example.healthsync.domain.model.StepSample
import com.example.healthsync.domain.repository.HealthDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HealthUiState(
    val isLoading: Boolean = false,
    val heartRate: List<HeartRateSample> = emptyList(),
    val steps: List<StepSample> = emptyList(),
    val sleep: List<SleepSession> = emptyList(),
    val exercises: List<ExerciseSession> = emptyList(),
    val syncError: String? = null,
)

@HiltViewModel
class HealthDataViewModel @Inject constructor(
    private val repository: HealthDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeHeartRate().collect { samples ->
                _uiState.update { it.copy(heartRate = samples) }
            }
        }
        viewModelScope.launch {
            repository.observeSteps().collect { samples ->
                _uiState.update { it.copy(steps = samples) }
            }
        }
        viewModelScope.launch {
            repository.observeSleep().collect { sessions ->
                _uiState.update { it.copy(sleep = sessions) }
            }
        }
        viewModelScope.launch {
            repository.observeExerciseSessions().collect { sessions ->
                _uiState.update { it.copy(exercises = sessions) }
            }
        }
    }

    fun syncAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, syncError = null) }
            runCatching { repository.syncAll() }
                .onFailure { e -> _uiState.update { it.copy(syncError = e.message) } }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(syncError = null) }
    }
}
