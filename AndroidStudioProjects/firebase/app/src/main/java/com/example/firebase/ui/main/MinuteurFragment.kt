package com.example.firebase.ui.main

import android.os.SystemClock
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.masuperappfirebase.ui.theme.MaSuperAppFirebaseTheme
import kotlinx.coroutines.delay

@Composable
fun ChronoScreen(modifier: Modifier = Modifier) {
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var timeBuffer by remember { mutableStateOf(0L) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var laps by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            elapsedTime = SystemClock.elapsedRealtime() - startTime + timeBuffer
            delay(10)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatTime(elapsedTime),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Button(onClick = {
                if (isRunning) {
                    timeBuffer += SystemClock.elapsedRealtime() - startTime
                    isRunning = false
                } else {
                    startTime = SystemClock.elapsedRealtime()
                    isRunning = true
                }
            }) {
                Text(if (isRunning) "Pause" else "Start")
            }

            Button(onClick = {
                isRunning = false
                startTime = 0L
                timeBuffer = 0L
                elapsedTime = 0L
                laps = emptyList()
            }) {
                Text("Reset")
            }

            Button(onClick = {
                if (isRunning) {
                    laps = listOf("Lap ${laps.size + 1}: ${formatTime(elapsedTime)}") + laps
                }
            }) {
                Text("Lap")
            }
        }

        LazyColumn {
            items(laps) { lap ->
                Text(text = lap, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val minutes = (ms / 1000) / 60
    val seconds = (ms / 1000) % 60
    val millis = (ms % 1000) % 1000 / 10
    return String.format("%02d:%02d.%02d", minutes, seconds, millis)
}

@Preview(showBackground = true)
@Composable
fun ChronoScreenPreview() {
    MaSuperAppFirebaseTheme {
        ChronoScreen()
    }
}
