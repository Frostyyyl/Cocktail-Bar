package com.example.cocktail_bar.components.layout

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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