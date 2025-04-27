package com.example.cocktail_bar.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
