package com.example.cocktail_bar


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cocktail_bar.ui.theme.CocktailBarTheme

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocktailBarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val cocktail: Cocktail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra("cocktail", Cocktail::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra("cocktail")
                    } ?: Cocktail( // In case of null reference
                        id = "0",
                        name = "Unknown Cocktail",
                        imageLink = "",
                        category = "Unknown",
                        alcoholic = "Unknown",
                        instructions = "No instructions available",
                        ingredients = arrayListOf(),
                        measurements = arrayListOf()
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        item {
                            ReturnButton()
                            Spacer(modifier = Modifier.size(16.dp))
                            CocktailDetails(
                                cocktail = cocktail
                            )
                            Spacer(modifier = Modifier.size(24.dp))
                            Timer(minutes = 1, seconds = 0)
                        }
                    }
                }
            }
        }
    }
}