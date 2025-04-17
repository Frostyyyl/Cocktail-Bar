package com.example.cocktail_bar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cocktail_bar.components.layout.AppBar
import com.example.cocktail_bar.ui.theme.CocktailBarTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CocktailActivityTemplate (
    title: String = "Cocktail Bar App",
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scope: CoroutineScope = rememberCoroutineScope(),
    drawerContent: (@Composable () -> Unit)? = {
        ModalDrawerSheet (
            drawerContainerColor = MaterialTheme.colorScheme.primary
        ){
            Text(
                "Cocktail Bar App",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider()
            NavigationDrawerItem(
                label = { Text(text = "Main page") },
                selected = false,
                onClick = { /*TODO*/ }
            )
        }
    },
    mainContent: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    CocktailBarTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                drawerContent?.invoke()
            },
            gesturesEnabled = true,

        ) {
            Scaffold(
                topBar = {
                    AppBar(
                        title = title,
                        onBackClick = {
                            scope.launch {
                                drawerState.apply {
                                    if(isClosed) open() else close()
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
