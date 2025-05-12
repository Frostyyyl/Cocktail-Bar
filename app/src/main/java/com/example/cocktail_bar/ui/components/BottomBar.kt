package com.example.cocktail_bar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomBar(
    pagerState: PagerState,
    scope: CoroutineScope
) {
    BottomAppBar (
        containerColor = MaterialTheme.colorScheme.secondary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val tileModifier = Modifier
                .weight(1f)
                .fillMaxHeight().padding(top = 8.dp)
            val tileButtonDefaults = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
            Button(
                onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                modifier = tileModifier,
                shape = AbsoluteRoundedCornerShape(16.dp),
                colors = tileButtonDefaults,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.Black)
            }

            Button(
                onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                modifier = tileModifier,
                shape = AbsoluteRoundedCornerShape(16.dp),
                colors = tileButtonDefaults,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Alcoholic")
            }

            Button(
                onClick = { scope.launch { pagerState.animateScrollToPage(2) } },
                modifier = tileModifier,
                shape = AbsoluteRoundedCornerShape(16.dp),
                colors = tileButtonDefaults,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Non-alcoholic")
            }
        }
    }
}