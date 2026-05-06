package com.example.healthsync.ui.health

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HealthPermissionScreen(
    onGranted: () -> Unit,
    viewModel: PermissionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val activity = LocalContext.current as? Activity

    LaunchedEffect(state) {
        if (state is PermissionViewModel.State.Granted) onGranted()
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            when (val s = state) {
                PermissionViewModel.State.Connecting -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(16.dp))
                        Text("Connecting to Samsung Health…")
                    }
                }
                PermissionViewModel.State.NeedsPermission -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Samsung Health access is required to read your biometric data.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = { activity?.let { viewModel.requestPermissions(it) } }) {
                            Text("Grant Access")
                        }
                    }
                }
                is PermissionViewModel.State.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = s.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedButton(onClick = viewModel::connect) {
                            Text("Retry")
                        }
                    }
                }
                PermissionViewModel.State.Granted -> Unit
            }
        }
    }
}
