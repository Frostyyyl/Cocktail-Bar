package com.example.cocktail_bar

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
fun SendSMSButton(
    cocktail: Cocktail,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    FloatingActionButton(
        onClick = {
            println(cocktail.name)
            scope.launch {
                snackbarHostState.showSnackbar("Sending SMS about ${cocktail.name}")
            }
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = Color.Black
    ) {
        Row {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send SMS button"
            )
        }
    }
}