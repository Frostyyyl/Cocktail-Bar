package com.example.cocktail_bar


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cocktail_bar.api.Cocktail
import com.example.cocktail_bar.api.RetrofitInstance
import com.example.cocktail_bar.components.CocktailDetails
import com.example.cocktail_bar.components.SendSMSButton
import com.example.cocktail_bar.components.Timer
import com.example.cocktail_bar.components.layout.ReturnButton

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val cocktailState = remember { mutableStateOf<Cocktail?>(null) }

            // Get the ID from intent
            val id = intent.getStringExtra("id")

            // Fetch cocktail details when ID is available
            LaunchedEffect(id) {
                if (id != null) {
                    try {
                        val response = RetrofitInstance.api.getCocktailById(id)
                        if (response.drinks.isNotEmpty()) {
                            cocktailState.value = response.drinks[0].toCocktail()
                        }
                    } catch (e: Exception) {
                        // Handle error
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
                    }
                }
            }

            CocktailActivityTemplate(
                snackBarHostState = snackBarHostState,
                scope = scope,
                mainContent = { innerPadding ->
                    cocktailState.value?.let { cocktail ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            // Details scrollable content
                            LazyColumn(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                item {
                                    ReturnButton()
                                    Spacer(modifier = Modifier.size(16.dp))
                                    CocktailDetails(cocktail = cocktail)
                                    Spacer(modifier = Modifier.size(24.dp))
                                    Timer(minutes = 1, seconds = 0)
                                }
                            }

                            // Send SMS button
                            SendSMSButton(
                                cocktailName = cocktail.name,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp),
                                snackBarHostState = snackBarHostState,
                                scope = scope
                            )
                        }
                    } ?: run {
                        // Show loading or error state
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Loading cocktail details...")
                        }
                    }
                }
            )
        }
    }
}