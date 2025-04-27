package com.example.cocktail_bar.data.service

import com.example.cocktail_bar.data.model.CocktailsDataList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {
    @GET("filter.php?a=Alcoholic")
    suspend fun getAlcoholicCocktailList(): CocktailsDataList

    @GET("filter.php?a=Non_Alcoholic")
    suspend fun getNonAlcoholicCocktailList(): CocktailsDataList

    @GET("lookup.php")
    suspend fun getCocktailById(@Query("i") id: String): CocktailsDataList
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