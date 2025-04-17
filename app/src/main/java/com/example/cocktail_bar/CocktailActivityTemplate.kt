package com.example.cocktail_bar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.cocktail_bar.components.layout.AppBar
import com.example.cocktail_bar.components.layout.NavigationDrawer
import com.example.cocktail_bar.ui.theme.CocktailBarTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CocktailActivityTemplate (
    title: String = "Cocktail Bar App",
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scope: CoroutineScope = rememberCoroutineScope(),
    mainContent: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    CocktailBarTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawer(
                    onBackClick = {
                        scope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                    }
                )
            },
            gesturesEnabled = true,

        ) {
            Scaffold(
                topBar = {
                    AppBar(
                        title = title,
                        onListClick = {
                            scope.launch {
                                drawerState.apply {
                                    open()
                                }
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxSize(),
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                }
            ) { innerPadding ->
                mainContent(innerPadding)
            }
        }
    }
}
