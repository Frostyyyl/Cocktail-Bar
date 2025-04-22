package com.example.cocktail_bar.components

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
import com.example.cocktail_bar.api.Cocktail
import com.example.cocktail_bar.api.CocktailShort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SendSMSButton(
    cocktailName: String,
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    FloatingActionButton(
        onClick = {
            println(cocktailName)
            scope.launch {
                snackBarHostState.showSnackbar("Sending SMS about ${cocktailName}")
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