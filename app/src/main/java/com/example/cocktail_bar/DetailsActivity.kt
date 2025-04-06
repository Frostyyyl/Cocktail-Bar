package com.example.cocktail_bar

import android.R.attr.category
import android.R.attr.fontWeight
import android.R.attr.onClick
import android.R.attr.rotation
import android.R.attr.text
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.cocktail_bar.ui.theme.CocktailBarTheme
import kotlinx.coroutines.delay

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocktailBarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val cocktail: Cocktail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra("cocktail", Cocktail::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra("cocktail")
                    } ?: Cocktail( // In case of null reference
                        id = "0",
                        name = "Unknown Cocktail",
                        imageLink = "",
                        category = "Unknown",
                        alcoholic = "Unknown",
                        instructions = "No instructions available",
                        ingredients = arrayListOf(),
                        measurements = arrayListOf()
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        item {
                            ReturnButton()
                            Spacer(modifier = Modifier.size(16.dp))
                            CocktailDetails(
                                cocktail = cocktail
                            )
                            Spacer(modifier = Modifier.size(24.dp))
                            Timer(minutes = 1, seconds = 0)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CocktailDetails(cocktail: Cocktail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = cocktail.name,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )

        AsyncImage(
            model = cocktail.imageLink,
            contentDescription = "Cocktail Image",
            modifier = Modifier
                .padding(16.dp)
                .width(200.dp)
                .height(200.dp)
                .align(Alignment.CenterHorizontally),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
        )

        Spacer(modifier = Modifier.size(4.dp))

        val style = SpanStyle(
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 20.sp
        )

        CocktailCategory(cocktail.category, style)
        CocktailIsAlcoholic(cocktail.alcoholic, style)
        CocktailIngredients(cocktail.ingredients, cocktail.measurements, style)
        CocktailInstructions(cocktail.instructions, style)
    }
}

@Composable
fun CocktailCategory(category: String, style: SpanStyle) {
    Text(
        buildAnnotatedString {
            withStyle(style = style) {
                append("Category: ")
            }
            append(category)
        }
    )
}

@Composable
fun CocktailIsAlcoholic(alcoholic: String, style: SpanStyle) {
    var isAlcoholic = "Optionally"

    if (alcoholic == "Alcoholic") {
        isAlcoholic = "Yes"
    } else if (alcoholic == "Non Alcoholic") {
        isAlcoholic == "No"
    }

    Text(
        buildAnnotatedString {
            withStyle(style = style) {
                append("Alcoholic: ")
            }
            append(isAlcoholic)
        }
    )
}

@Composable
fun CocktailIngredients(ingredients: List<String>, measurements: List<String>, style: SpanStyle){
    Text(
        buildAnnotatedString {
            withStyle(style = style) {
                append("Ingredients: \n")
            }
            for (i in measurements.indices) {
                append("■ ${measurements[i]} of ${ingredients[i]}\n")
            }
        }
    )
}

@Composable
fun CocktailInstructions(instructions: String, style: SpanStyle) {
    Text(
        buildAnnotatedString {
            withStyle(style = style) {
                append("Instructions: ")
            }
            append(instructions.replaceFirstChar { it.uppercase() })
        }
    )
}

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

@Composable
fun ReturnButton() {
    val context = LocalContext.current

    Button(
        onClick = { (context as Activity).finish() },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Return",
            modifier = Modifier.rotate(180f),
            tint = Color.Black
        )
    }
}