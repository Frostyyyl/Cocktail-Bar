package com.example.cocktail_bar.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "Cocktail Bar",
    onListClick: (() -> Unit)
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState());

    TopAppBar(
        title = {
            Text(
                title,
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(onClick = onListClick) {
                Icon(Icons.AutoMirrored.Filled.List,
                    contentDescription = "Navigation Drawer",
                    tint = Color.Black)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier.shadow(10.dp),
        scrollBehavior = scrollBehavior
    )
}