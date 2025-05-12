package com.example.cocktail_bar.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.cocktail_bar.data.service.CocktailViewModel
import com.example.cocktail_bar.ui.components.BottomBar
import com.example.cocktail_bar.ui.components.TopBar
import com.example.cocktail_bar.ui.screens.alcoholic.AlcoholicScreen
import com.example.cocktail_bar.ui.screens.home.HomeScreen
import com.example.cocktail_bar.ui.screens.nonalcoholic.NonAlcoholicScreen
import com.example.cocktail_bar.ui.theme.CocktailBarTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: CocktailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val selectedPage = rememberSaveable { mutableIntStateOf(intent?.getIntExtra("selectedPage", 0) ?: 0) }
            val pagerState = rememberPagerState(initialPage = selectedPage.intValue, pageCount = { 3 })
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
                                    drawerState.close()
                                }
                            },
                            onNavigationItemSelected = { page ->
                                scope.launch {
                                    pagerState.animateScrollToPage(page)
                                    selectedPage.value = page
                                    drawerState.close()
                                }
                            }
                        )
                    },
                    gesturesEnabled = true
                ) {
                    Scaffold(
                        topBar = {
                            TopBar(
                                title = "Cocktail Bar",
                                onListClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            BottomBar(
                                pagerState = pagerState,
                                scope = scope
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(snackBarHostState)
                        }
                    ) { innerPadding ->
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) { page ->
                            when (page) {
                                0 -> HomeScreen()
                                1 -> AlcoholicScreen(
                                    viewModel = viewModel,
                                    snackBarHostState = snackBarHostState,
                                    scope = scope
                                )
                                2 -> NonAlcoholicScreen(
                                    viewModel = viewModel,
                                    snackBarHostState = snackBarHostState,
                                    scope = scope
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
