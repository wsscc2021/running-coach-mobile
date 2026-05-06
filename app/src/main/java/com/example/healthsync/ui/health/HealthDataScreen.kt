package com.example.healthsync.ui.health

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthDataScreen(viewModel: HealthDataViewModel = hiltViewModel()) {
    val heartRate by viewModel.heartRate.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Health Data") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = viewModel::syncAll) {
                Text("Sync Now")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text("Heart rate readings: ${heartRate.size}")
        }
    }
}
