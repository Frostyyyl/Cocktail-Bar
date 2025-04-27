package com.example.cocktail_bar.ui.screens.details

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cocktail_bar.ui.components.CocktailDetails
import com.example.cocktail_bar.ui.components.ShareButton
import com.example.cocktail_bar.ui.components.Timer


class DetailsActivity : ComponentActivity() {
    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            val id = intent.getStringExtra("id") ?: ""

            LaunchedEffect(id) {
                detailsViewModel.fetchCocktailDetails(id)
            }

            DetailsTemplate(
                title = "Cocktail Details",
                snackBarHostState = snackBarHostState,
                scope = scope,
                onNavigationItemSelected = { page ->
                    val resultIntent = Intent().apply {
                        putExtra("selectedPage", page)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                },
                mainContent = { innerPadding ->
                    if (detailsViewModel.isLoading.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Loading details...")
                        }
                    } else {
                        detailsViewModel.cocktailState.value?.let { cocktail ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Spacer(modifier = Modifier.size(16.dp))
                                    CocktailDetails(cocktail = cocktail)
                                    Spacer(modifier = Modifier.size(24.dp))
                                    Timer(minutes = 1, seconds = 0)
                                    Spacer(modifier = Modifier.size(80.dp))
                                }

                                ShareButton(
                                    cocktailName = cocktail.name,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(horizontal = 24.dp, vertical = 12.dp),
                                    snackBarHostState = snackBarHostState,
                                    scope = scope
                                )
                            }
                        } ?: run {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("An error occurred while loading details.")
                            }
                        }
                    }
                }
            )
        }
    }
}