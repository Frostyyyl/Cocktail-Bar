package com.example.cocktail_bar.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.cocktail_bar.data.service.CocktailViewModel
import com.example.cocktail_bar.ui.screens.alcoholic.AlcoholicScreen
import com.example.cocktail_bar.ui.screens.home.HomeScreen
import com.example.cocktail_bar.ui.screens.nonalcoholic.NonAlcoholicScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: CocktailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val pagerState = rememberPagerState(pageCount = { 3 })

            // Handle the selected page from intent
            val selectedPage = intent?.getIntExtra("selectedPage", 0) ?: 0
            LaunchedEffect(selectedPage) {
                if (selectedPage != pagerState.currentPage) {
                    pagerState.scrollToPage(selectedPage)
                }
            }

            MainTemplate(
                snackBarHostState = snackBarHostState,
                scope = scope,
                pagerState = pagerState,
                onNavigationItemSelected = { page ->
                    scope.launch {
                        pagerState.animateScrollToPage(page)
                    }
                },
                mainContent = { innerPadding ->
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) { page ->
                        when(page) {
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
            )
        }
    }
}