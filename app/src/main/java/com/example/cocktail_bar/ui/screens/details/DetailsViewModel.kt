package com.example.cocktail_bar.ui.screens.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktail_bar.data.model.Cocktail
import com.example.cocktail_bar.data.service.RetrofitInstance
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    val cocktailState = mutableStateOf<Cocktail?>(null)
    val isLoading = mutableStateOf(true)

    fun fetchCocktailDetails(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCocktailById(id)
                if (response.drinks.isNotEmpty()) {
                    cocktailState.value = response.drinks[0].toCocktail()
                }
            } catch (e: Exception) {
                cocktailState.value = Cocktail(
                    id = "0",
                    name = "Error loading cocktail",
                    imageLink = "",
                    category = "Error",
                    alcoholic = "Error",
                    instructions = "Could not load cocktail details",
                    ingredients = emptyList(),
                    measurements = emptyList()
                )
            } finally {
                isLoading.value = false
            }
        }
    }
}
