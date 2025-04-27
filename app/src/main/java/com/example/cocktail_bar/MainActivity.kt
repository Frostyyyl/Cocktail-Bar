package com.example.cocktail_bar

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.cocktail_bar.data.service.CocktailViewModel
import com.example.cocktail_bar.ui.screens.alcoholic.AlcoholicScreen
import com.example.cocktail_bar.ui.screens.home.HomeScreen
import com.example.cocktail_bar.ui.screens.nonalcoholic.NonAlcoholicScreen

class MainActivity : ComponentActivity() {
    private val viewModel: CocktailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val pagerState = rememberPagerState(pageCount = {
                3
            })

            ActivityTemplate (
                snackBarHostState = snackBarHostState,
                scope = scope,
                pagerState = pagerState,
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