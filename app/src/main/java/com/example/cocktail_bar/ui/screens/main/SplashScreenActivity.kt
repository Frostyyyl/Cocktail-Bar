package com.example.cocktail_bar.ui.screens.main


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
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
    val imageViewRef = remember { mutableStateOf<ImageView?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                ImageView(ctx).apply {
                    setImageResource(R.drawable.ic_launcher_foreground)
                    scaleX = 0f
                    scaleY = 0f
                    imageViewRef.value = this
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        imageViewRef.value?.let { imageView ->
            // Run animations
            val animatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 0f, 1f).apply {
                duration = 1000
                interpolator = AccelerateDecelerateInterpolator()
            }
            val animatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 0f, 1f).apply {
                duration = 1000
                interpolator = AccelerateDecelerateInterpolator()
            }

            animatorX.start()
            animatorY.start()

            delay(1500)
            onAnimationFinished()
        }
    }
}