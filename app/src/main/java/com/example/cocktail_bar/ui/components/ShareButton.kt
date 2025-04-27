package com.example.cocktail_bar.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ShareButton(
    cocktailName: String,
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    FloatingActionButton(
        onClick = {
            println(cocktailName)
            scope.launch {
                snackBarHostState.showSnackbar("Sharing the recipe of $cocktailName")
            }
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = Color.Black
    ) {
        Row {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Share button"
            )
        }
    }
}