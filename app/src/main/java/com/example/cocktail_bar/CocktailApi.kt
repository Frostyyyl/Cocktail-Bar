package com.example.cocktail_bar

import android.R.attr.data
import android.os.Parcelable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import kotlinx.parcelize.Parcelize
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.collections.firstOrNull
import kotlin.jvm.java

interface CocktailApi {
    @GET("random.php")
    suspend fun getRandomCocktail(): CocktailsDataList
}

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

class CocktailViewModel : ViewModel() {
    private val _cocktails = mutableStateOf<List<Cocktail>>(emptyList())
    val cocktails: State<List<Cocktail>> = _cocktails

    fun fetchRandomCocktails(num: Int) {
        viewModelScope.launch {
            val newCocktails = mutableListOf<Cocktail>()

            for (i in 1..num) {
                try {
                    val response = RetrofitInstance.api.getRandomCocktail()
                    newCocktails.add(response.drinks[0].toCocktail())
                } catch (e: Exception) {
                    println("Error fetching cocktail: ${e.message}")
                }
            }

            _cocktails.value = newCocktails
        }
    }
}

data class CocktailsDataList(
    val drinks: List<CocktailData>
)

@Parcelize
data class Cocktail(
    val id: String,
    val name: String,
    val imageLink: String,
    val category: String,
    val alcoholic: String,
    val instructions: String,
    val ingredients: List<String>,
    val measurements: List<String>
) : Parcelable

data class CocktailData(
    val idDrink: String,
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
) {
    fun toCocktail(): Cocktail {
        return Cocktail(
            id = idDrink,
            name = strDrink,
            imageLink = strDrinkThumb,
            category = strCategory,
            alcoholic = strAlcoholic,
            instructions = strInstructions,
            ingredients = listOfNotNull(
                strIngredient1,
                strIngredient2,
                strIngredient3,
                strIngredient4,
                strIngredient5,
                strIngredient6,
                strIngredient7,
                strIngredient8,
                strIngredient9,
                strIngredient10,
                strIngredient11,
                strIngredient12,
                strIngredient13,
                strIngredient14,
                strIngredient15
            ).filter { it.isNotBlank() },
            measurements = listOfNotNull(
                strMeasure1,
                strMeasure2,
                strMeasure3,
                strMeasure4,
                strMeasure5,
                strMeasure6,
                strMeasure7,
                strMeasure8,
                strMeasure9,
                strMeasure10,
                strMeasure11,
                strMeasure12,
                strMeasure13,
                strMeasure14,
                strMeasure15,
            ).filter { it.isNotBlank() }
        )
    }
}