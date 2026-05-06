package com.example.healthsync.ui.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthsync.domain.model.ExerciseSession
import com.example.healthsync.domain.model.HeartRateSample
import com.example.healthsync.domain.model.SleepSession
import com.example.healthsync.domain.model.StepSample

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthDataScreen(viewModel: HealthDataViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.syncError) {
        uiState.syncError?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Health Data") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { if (!uiState.isLoading) viewModel.syncAll() },
                text = { Text(if (uiState.isLoading) "Syncing…" else "Sync Now") },
                icon = {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { MetricCard("Heart Rate") { HeartRateContent(uiState.heartRate) } }
            item { MetricCard("Steps") { StepsContent(uiState.steps) } }
            item { MetricCard("Sleep") { SleepContent(uiState.sleep) } }
            item { MetricCard("Exercise") { ExerciseContent(uiState.exercises) } }
        }
    }
}

@Composable
private fun MetricCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun HeartRateContent(samples: List<HeartRateSample>) {
    if (samples.isEmpty()) {
        EmptyState()
    } else {
        Text("${samples.first().bpm} bpm", style = MaterialTheme.typography.headlineMedium)
        Text("${samples.size} readings today", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun StepsContent(samples: List<StepSample>) {
    if (samples.isEmpty()) {
        EmptyState()
    } else {
        Text("${samples.sumOf { it.count }} steps", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun SleepContent(sessions: List<SleepSession>) {
    if (sessions.isEmpty()) {
        EmptyState()
    } else {
        val last = sessions.first()
        val hours = (last.endTime - last.startTime) / 3_600_000f
        Text("%.1fh".format(hours), style = MaterialTheme.typography.headlineMedium)
        Text("Last session", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ExerciseContent(sessions: List<ExerciseSession>) {
    if (sessions.isEmpty()) {
        EmptyState()
    } else {
        sessions.take(3).forEach { session ->
            val minutes = (session.endTime - session.startTime) / 60_000
            Text(
                "${session.exerciseType}  —  ${minutes}min",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Text("No data", style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant)
}
