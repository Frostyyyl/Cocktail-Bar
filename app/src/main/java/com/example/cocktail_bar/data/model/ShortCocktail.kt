package com.example.cocktail_bar.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShortCocktail(
    val id: String,
    val name: String,
    val imageLink: String,
) : Parcelable
