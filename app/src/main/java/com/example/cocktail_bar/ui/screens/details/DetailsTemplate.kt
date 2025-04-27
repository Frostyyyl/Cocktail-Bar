package com.example.cocktail_bar.ui.screens.details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import com.example.cocktail_bar.ui.components.NavigationDrawer
import com.example.cocktail_bar.ui.components.TopBar
import com.example.cocktail_bar.ui.theme.CocktailBarTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DetailsTemplate(
    title: String = "Cocktail Bar",
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scope: CoroutineScope = rememberCoroutineScope(),
    pagerState: PagerState = rememberPagerState(pageCount = { 3 }),
    onNavigationItemSelected: (Int) -> Unit = {},
    mainContent: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    CocktailBarTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawer(
                    pagerState = pagerState,
                    scope = scope,
                    onBackClick = {
                        scope.launch {
                            drawerState.apply { close() }
                        }
                    },
                    onNavigationItemSelected = onNavigationItemSelected
                )
            },
            gesturesEnabled = true,
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        title = title,
                        onListClick = {
                            scope.launch {
                                drawerState.apply { open() }
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxSize(),
                snackbarHost = {
                    SnackbarHost(snackBarHostState)
                }
            ) { innerPadding ->
                mainContent(innerPadding)
            }
        }
    }
}