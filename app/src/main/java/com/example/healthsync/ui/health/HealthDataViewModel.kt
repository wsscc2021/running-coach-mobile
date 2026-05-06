package com.example.healthsync.ui.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthsync.domain.repository.HealthDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthDataViewModel @Inject constructor(
    private val repository: HealthDataRepository,
) : ViewModel() {

    val heartRate = repository.observeHeartRate()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val steps = repository.observeSteps()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun syncAll() {
        viewModelScope.launch { repository.syncAll() }
    }
}
