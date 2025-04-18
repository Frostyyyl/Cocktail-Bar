package com.example.cocktail_bar

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cocktail_bar.api.CocktailViewModel
import com.example.cocktail_bar.components.CocktailDetails
import com.example.cocktail_bar.components.CocktailList
import com.example.cocktail_bar.components.RefreshButton
import com.example.cocktail_bar.components.SendSMSButton
import com.example.cocktail_bar.components.Timer
import com.example.cocktail_bar.components.cards.AlcoholicCategoryCard
import com.example.cocktail_bar.components.cards.HomeCard
import com.example.cocktail_bar.components.cards.NonAlcoholicCategoryCard
import com.example.cocktail_bar.utility.isTablet

const val cocktailsNum = 16

class MainActivity : ComponentActivity() {
    private val viewModel: CocktailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //CocktailApp(viewModel)

            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val pagerState = rememberPagerState(pageCount = {
                3
            })

            CocktailActivityTemplate (
                snackbarHostState = snackbarHostState,
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
                            0 -> HomeCard()
                            1 -> AlcoholicCategoryCard(
                                viewModel = viewModel,
                                snackbarHostState = snackbarHostState,
                                scope = scope
                            )
                            2 -> NonAlcoholicCategoryCard(
                                viewModel = viewModel,
                                snackbarHostState = snackbarHostState,
                                scope = scope
                            )
                        }
                    }
                }
            )
        }
    }
}