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
import com.example.cocktail_bar.api.CocktailViewModel
import com.example.cocktail_bar.components.cards.AlcoholicCategoryCard
import com.example.cocktail_bar.components.cards.HomeCard
import com.example.cocktail_bar.components.cards.NonAlcoholicCategoryCard

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

            CocktailActivityTemplate (
                snackBarHostState = snackBarHostState,
                scope = scope,
                pagerState = pagerState,
                mainContent = { innerPadding ->
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding) // is this useful for anything?
                    ) { page ->
                        when(page) {
                            0 -> HomeCard()
                            1 -> AlcoholicCategoryCard(
                                viewModel = viewModel,
                                snackBarHostState = snackBarHostState,
                                scope = scope
                            )
                            2 -> NonAlcoholicCategoryCard(
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