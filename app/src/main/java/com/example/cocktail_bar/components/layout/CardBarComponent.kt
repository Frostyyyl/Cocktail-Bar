package com.example.cocktail_bar.components.layout

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CardBar(
    pagerState: PagerState,
    scope: CoroutineScope
) {
    BottomAppBar (
        actions = {
            IconButton(onClick = {
                scope.launch { pagerState.animateScrollToPage(0) }
            }) {
                Icon(Icons.Filled.Home, contentDescription = "Home")
            }
            Button(onClick = {
                scope.launch { pagerState.animateScrollToPage(1) }
            }) {
                Text("Alcoholic")
            }
            Button(onClick = {
                scope.launch { pagerState.animateScrollToPage(2) }
            }) {
                Text("Non-alcoholic")
            }
        },
        containerColor = MaterialTheme.colorScheme.secondary
    )
}