package com.example.cocktail_bar

import android.R
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.jvm.java

object RetrofitInstance {
    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

    val api: CocktailApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CocktailApi::class.java)
    }
}

interface CocktailApi {
    @GET("random.php")
    suspend fun getRandomCocktail(): Drinks

    @GET("search.php")
    suspend fun getCocktailDetails(@Query("s") cocktailName: String) : Details
}

class CocktailViewModel : ViewModel() {
    private val _cocktails = mutableStateOf(Drinks(emptyList())) // Initialize properly
    val cocktails: State<Drinks> = _cocktails

    fun fetchRandomCocktails(num: Int = 10) {
        viewModelScope.launch {
            val newDrinks = mutableListOf<Drink>()

            for (i in 1..num) {
                try {
                    val response = RetrofitInstance.api.getRandomCocktail()
                    response.drinks?.firstOrNull()?.let { drink ->
                        newDrinks.add(drink)
                    }
                } catch (e: Exception) {
                    println("Error fetching cocktail: ${e.message}")
                }
            }

            _cocktails.value = Drinks(drinks = newDrinks)
        }
    }

    private val _cocktailDetails = mutableStateOf<DrinkDetails?>(null) // Initialize properly
    val cocktailDetails: State<DrinkDetails?> = _cocktailDetails

    fun fetchCocktailDetails(cocktailName: String){
        viewModelScope.launch {
            try {
                // Fetch cocktail details based on cocktail name
                val response = RetrofitInstance.api.getCocktailDetails(cocktailName)
                response.drinks?.firstOrNull()?.let { drink ->
                    _cocktailDetails.value = drink
                }
            } catch (e: Exception) {
                // Handle error
                println("Error fetching cocktail details: ${e.message}")
            }
        }
    }
}



data class Drinks(
    val drinks: List<Drink>?
)

data class Drink(
    val strDrink: String,
    val strDrinkThumb: String,
    val strCategory: String,
    val strAlcoholic: String
)

data class Details(
    val drinks: List<DrinkDetails>?
)

data class DrinkDetails(
    val strDrink: String,
    val strDrinkThumb: String,
    val strCategory: String,
    val strAlcoholic: String,
    val strInstructions: String,

    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,

    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?,
)