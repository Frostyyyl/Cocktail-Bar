package com.example.cocktail_bar.ui.screens.main


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cocktail_bar.R
import com.example.cocktail_bar.ui.theme.CocktailBarTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CocktailBarTheme {
                SplashScreen(
                    onAnimationFinished = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val rotation = remember { Animatable(0f) }

    // Launch rotation animation
    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 1800f,
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
        )
        delay(200)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.radialGradient(listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.background))),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.cocktail_icon),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    rotationZ = rotation.value
                }
                .clip(CircleShape)
        )
    }
}
