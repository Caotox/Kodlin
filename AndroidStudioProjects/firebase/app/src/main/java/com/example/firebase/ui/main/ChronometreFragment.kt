package com.example.firebase.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer.ui.theme.TimerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TimerScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TimerScreen(modifier: Modifier = Modifier) {
    var selectedMinutes by remember { mutableStateOf(2) }
    var selectedSeconds by remember { mutableStateOf(30) }
    var timeInSeconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isTimerFinished by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val totalDuration = selectedMinutes * 60 + selectedSeconds

    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    fun startStopTimer() {
        if (isRunning) {
            isRunning = false
        } else {
            if (timeInSeconds == 0 || isTimerFinished) {
                timeInSeconds = totalDuration
                isTimerFinished = false
            }
            isRunning = true
            coroutineScope.launch {
                while (isRunning && timeInSeconds > 0) {
                    delay(1000L)
                    timeInSeconds--
                }
                isRunning = false
                if (timeInSeconds == 0) {
                    isTimerFinished = true
                }
            }
        }
    }

    fun resetTimer() {
        timeInSeconds = totalDuration
        isRunning = false
        isTimerFinished = false
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Définir la durée",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        DurationSelector(
            label = "Minutes",
            value = selectedMinutes,
            onDecrement = { if (selectedMinutes > 0) selectedMinutes-- },
            onIncrement = { selectedMinutes++ },
            enabled = !isRunning
        )

        Spacer(modifier = Modifier.height(16.dp))

        DurationSelector(
            label = "Secondes",
            value = selectedSeconds,
            onDecrement = { if (selectedSeconds > 0) selectedSeconds-- },
            onIncrement = { if (selectedSeconds < 59) selectedSeconds++ },
            enabled = !isRunning
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = formatTime(timeInSeconds),
            fontSize = 48.sp,
            style = MaterialTheme.typography.headlineLarge
        )

        if (isTimerFinished) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Temps écoulé !",
                color = MaterialTheme.colorScheme.error,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { startStopTimer() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                ),
                enabled = (totalDuration > 0 || isRunning) && !isTimerFinished
            ) {
                Text(if (isRunning) "Pause" else "Start")
            }

            Button(
                onClick = { resetTimer() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Reset")
            }
        }
    }
}

@Composable
fun DurationSelector(
    label: String,
    value: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    enabled: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onDecrement,
                enabled = enabled,
                modifier = Modifier.padding(4.dp)
            ) {
                Text("-", fontSize = 20.sp)
            }

            Text(
                text = "%02d".format(value),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onIncrement,
                enabled = enabled,
                modifier = Modifier.padding(4.dp)
            ) {
                Text("+", fontSize = 20.sp)
            }
        }
    }
}
