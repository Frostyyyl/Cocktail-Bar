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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cocktail_bar.api.Cocktail
import com.example.cocktail_bar.components.CocktailDetails
import com.example.cocktail_bar.components.layout.ReturnButton
import com.example.cocktail_bar.components.SendSMSButton
import com.example.cocktail_bar.components.Timer
import com.example.cocktail_bar.ui.theme.CocktailBarTheme

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocktailBarTheme {
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { innerPadding ->
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

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Details scrollable content
                        LazyColumn(
                            modifier = Modifier
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

                        // Send SMS button
                        SendSMSButton(
                            cocktail = cocktail,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            snackbarHostState = snackbarHostState,
                            scope = scope
                        )
                    }
                }
            }
        }
    }
}