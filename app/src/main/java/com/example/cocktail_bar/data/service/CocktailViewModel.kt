package com.example.cocktail_bar.data.service

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktail_bar.data.model.Cocktail
import com.example.cocktail_bar.data.model.ShortCocktail
import kotlinx.coroutines.launch

class CocktailViewModel : ViewModel() {
    private val _isAlcoholicLoading = mutableStateOf(false)
    private val _isNonAlcoholicLoading = mutableStateOf(false)

    val isAlcoholicLoading: State<Boolean> = _isAlcoholicLoading
    val isNonAlcoholicLoading: State<Boolean> = _isNonAlcoholicLoading
    var alcoholicCocktails: List<ShortCocktail> = emptyList()
    var nonAlcoholicCocktails: List<ShortCocktail> = emptyList()

    init {
        fetchAlcoholicCocktails()
        fetchNonAlcoholicCocktails()
    }

    private fun fetchAlcoholicCocktails() {
        viewModelScope.launch {
            _isAlcoholicLoading.value = true
            val newCocktails = mutableListOf<ShortCocktail>()

            try {
                val response = RetrofitInstance.api.getAlcoholicCocktailList().drinks

                for (data in response) {
                    newCocktails.add(data.toShortCocktail())
                }
                alcoholicCocktails = newCocktails
            } catch (e: Exception) {
                println("Error fetching alcoholic cocktails: ${e.message}")
            } finally {
                _isAlcoholicLoading.value = false
            }
        }
    }

    private fun fetchNonAlcoholicCocktails() {
        viewModelScope.launch {
            _isNonAlcoholicLoading.value = true
            val newCocktails = mutableListOf<ShortCocktail>()

            try {
                val response = RetrofitInstance.api.getNonAlcoholicCocktailList().drinks

                for (data in response) {
                    newCocktails.add(data.toShortCocktail())
                }

                nonAlcoholicCocktails = newCocktails
            } catch (e: Exception) {
                println("Error fetching non-alcoholic cocktails: ${e.message}")
            } finally {
                _isNonAlcoholicLoading.value = false
            }
        }
    }

    fun fetchCocktailDetails(id: String, onSuccess: (Cocktail) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCocktailById(id)
                if (response.drinks.isNotEmpty()) {
                    onSuccess(response.drinks[0].toCocktail())
                }
            } catch (e: Exception) {
                println("Error fetching cocktail details: ${e.message}")
            }
        }
    }
}