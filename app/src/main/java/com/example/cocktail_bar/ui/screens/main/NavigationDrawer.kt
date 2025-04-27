package com.example.cocktail_bar.ui.screens.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    pagerState: PagerState,
    scope: CoroutineScope,
    onBackClick: () -> Unit,
    onNavigationItemSelected: (Int) -> Unit = {}
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                "Cocktail Bar",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimary
        )
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            },
            selected = false,
            onClick = {
                scope.launch {
                    onBackClick()
                    onNavigationItemSelected(0)
                }
            }
        )
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Alcoholic cocktails",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            },
            selected = false,
            onClick = {
                scope.launch {
                    onBackClick()
                    onNavigationItemSelected(1)
                }
            }
        )
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Non-Alcoholic cocktails",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            },
            selected = false,
            onClick = {
                scope.launch {
                    onBackClick()
                    onNavigationItemSelected(2)
                }
            }
        )
//        HorizontalDivider(
//            color = MaterialTheme.colorScheme.onPrimary
//        )
//        NavigationDrawerItem(
//            label = {
//                Text(
//                    text = "Timer",
//                    style = MaterialTheme.typography.titleMedium,
//                    color = Color.Black
//                )
//            },
//            selected = false,
//            onClick = {
//                // Handle Timer case if needed
//            }
//        )
    }
}