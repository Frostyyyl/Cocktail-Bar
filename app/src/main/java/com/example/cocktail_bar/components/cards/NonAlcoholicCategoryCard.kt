package com.example.cocktail_bar.components.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cocktail_bar.api.Cocktail
import com.example.cocktail_bar.api.CocktailViewModel
import com.example.cocktail_bar.components.CocktailDetails
import com.example.cocktail_bar.components.CocktailList
import com.example.cocktail_bar.components.LoadingIndicator
import com.example.cocktail_bar.components.SendSMSButton
import com.example.cocktail_bar.components.Timer
import com.example.cocktail_bar.utility.isTablet
import kotlinx.coroutines.CoroutineScope

@Composable
fun NonAlcoholicCategoryCard(
    viewModel: CocktailViewModel,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val cocktails = viewModel.nonAlcoholicCocktails
    val isLoading = viewModel.isNonAlcoholicLoading.value
    var selectedCocktail by remember { mutableIntStateOf(0) }
    var cocktailDetails by remember { mutableStateOf<Cocktail?>(null) }
    val scrollState = rememberScrollState(initial = 0)

    LaunchedEffect(cocktails) {
        if (cocktails.isNotEmpty()) {
            selectedCocktail = 0
            viewModel.fetchCocktailDetails(cocktails[0].idDrink) { cocktail ->
                cocktailDetails = cocktail
            }
        }
    }

    LaunchedEffect(selectedCocktail) {
        if (cocktails.isNotEmpty()) {
            viewModel.fetchCocktailDetails(cocktails[selectedCocktail].idDrink) { cocktail ->
                cocktailDetails = cocktail
            }
        }
    }

    if (isLoading) {
        LoadingIndicator()
    } else {
        if (isTablet()) {
            Row {
                CocktailList(Modifier.weight(3f), cocktails) { index: Int ->
                    selectedCocktail = index
                }
                Box(
                    Modifier
                        .weight(4f)
                        .padding(end = 16.dp)
                        .align(Alignment.CenterVertically)
                        .verticalScroll(scrollState)
                ) {
                    if (cocktailDetails != null) {
                        Column {
                            CocktailDetails(cocktail = cocktailDetails!!)
                            Spacer(modifier = Modifier.size(24.dp))
                            Timer(key = selectedCocktail, minutes = 1, seconds = 0)
                        }
                    }
                }
            }

            if (cocktails.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SendSMSButton(
                        cocktailName = cocktails[selectedCocktail].strDrink,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        snackBarHostState = snackBarHostState,
                        scope = scope
                    )
                }
            }
        } else {
            CocktailList(cocktails = cocktails) { index ->
                selectedCocktail = index
            }
        }
    }
}