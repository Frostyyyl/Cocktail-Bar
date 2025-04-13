package com.example.cocktail_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun Timer(key: Int = 0, minutes: Int, seconds: Int) {
    var startTimeMinutes by rememberSaveable(key) { mutableIntStateOf(minutes) }
    var startTimeSeconds by rememberSaveable(key) { mutableIntStateOf(seconds) }
    var timeLeftMinutes by rememberSaveable(key) { mutableIntStateOf(minutes) }
    var timeLeftSeconds by rememberSaveable(key) { mutableIntStateOf(seconds) }

    var isRunning by rememberSaveable(key) { mutableStateOf(false) }
    var isReset by rememberSaveable(key) { mutableStateOf(true) }

    var lastUpdateTime by rememberSaveable(key) { mutableLongStateOf(0L) }
    var totalPausedTime by rememberSaveable(key) { mutableLongStateOf(0L) }

    val mediumPadding = 26.dp
    val bigPadding = 20.dp


    LaunchedEffect(Unit) {
        if (isRunning) {
            lastUpdateTime = System.currentTimeMillis() - totalPausedTime
        }
    }

    // Timer logic
    LaunchedEffect(isRunning) {
        if (isRunning) {
            lastUpdateTime = System.currentTimeMillis()

            while (isRunning && (timeLeftMinutes > 0 || timeLeftSeconds > 0)) {
                delay(1000)

                val currentTime = System.currentTimeMillis()
                val elapsed = currentTime - lastUpdateTime - totalPausedTime
                val secondsPassed = (elapsed / 1000).toInt()

                if (secondsPassed > 0) {
                    val totalSeconds = timeLeftMinutes * 60 + timeLeftSeconds
                    val newTotalSeconds = (totalSeconds - secondsPassed).coerceAtLeast(0)

                    timeLeftMinutes = newTotalSeconds / 60
                    timeLeftSeconds = newTotalSeconds % 60

                    lastUpdateTime = currentTime
                    totalPausedTime = 0
                }
            }

            if (timeLeftMinutes == 0 && timeLeftSeconds == 0) {
                isRunning = false
            }
        }
    }

    fun updateTime(minutes: Int, seconds: Int) {
        if (!isRunning) {
            if (isReset) {
                startTimeMinutes = minutes.coerceAtLeast(0)
                startTimeSeconds = seconds.coerceIn(0, 59)
            }
            timeLeftMinutes = minutes.coerceAtLeast(0)
            timeLeftSeconds = seconds.coerceIn(0, 59)
            lastUpdateTime = System.currentTimeMillis()
            totalPausedTime = 0
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Timer",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.size(bigPadding))

        // Time increment buttons
        Row(verticalAlignment = Alignment.CenterVertically) {
            TimeButton(Icons.Default.KeyboardArrowUp) {
                updateTime(timeLeftMinutes + 1, timeLeftSeconds)
            }
            Spacer(modifier = Modifier.size(mediumPadding))
            TimeButton(Icons.Default.KeyboardArrowUp) {
                updateTime(timeLeftMinutes, timeLeftSeconds + 1)
            }
        }

        // Time display
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = timeLeftMinutes.toString().padStart(2, '0'),
                onValueChange = {
                    startTimeMinutes = (it.filter { c -> c.isDigit() }.take(2).ifEmpty { "0" }).toInt()
                    timeLeftMinutes = startTimeMinutes
                },
                modifier = Modifier.width(48.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                ),
                enabled = !isRunning
            )
            Text(text = ":", fontSize = 28.sp)
            BasicTextField(
                value = timeLeftSeconds.toString().padStart(2, '0'),
                onValueChange = {
                    val secs = it.filter { c -> c.isDigit() }.take(2).ifEmpty { "0" }.toInt()
                    startTimeSeconds = secs.coerceAtMost(59)
                    timeLeftSeconds = startTimeSeconds
                },
                modifier = Modifier.width(48.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                ),
                enabled = !isRunning
            )
        }

        // Time decrement buttons
        Row(verticalAlignment = Alignment.CenterVertically) {
            TimeButton(Icons.Default.KeyboardArrowDown) {
                updateTime(timeLeftMinutes - 1, timeLeftSeconds)
            }
            Spacer(modifier = Modifier.size(mediumPadding))
            TimeButton(Icons.Default.KeyboardArrowDown) {
                updateTime(timeLeftMinutes, timeLeftSeconds - 1)
            }
        }

        Spacer(modifier = Modifier.size(bigPadding))

        // Control buttons
        Row {
            // Play
            Button(
                onClick = {
                    isRunning = true
                    if (totalPausedTime > 0) {
                        // Adjust for time spent paused
                        lastUpdateTime = System.currentTimeMillis() - totalPausedTime
                        totalPausedTime = 0
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.size(bigPadding))

            // Pause
            Button(
                onClick = {
                    isRunning = false
                    isReset = false
                    totalPausedTime = System.currentTimeMillis() - lastUpdateTime - totalPausedTime
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Row {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(16.dp)
                            .background(Color.Black)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(16.dp)
                            .background(Color.Black)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

            Spacer(modifier = Modifier.size(bigPadding))

            // Reset
            Button(
                onClick = {
                    isRunning = false
                    isReset = true
                    timeLeftMinutes = startTimeMinutes
                    timeLeftSeconds = startTimeSeconds
                    lastUpdateTime = System.currentTimeMillis()
                    totalPausedTime = 0
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun TimeButton(imageVector: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(20.dp).width(30.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Time Button",
            modifier = Modifier.size(12.dp),
            tint = Color.Black
        )
    }
}