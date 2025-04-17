package com.example.cocktail_bar

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cocktail_bar.api.CocktailViewModel
import com.example.cocktail_bar.components.layout.AppBar
import com.example.cocktail_bar.components.CocktailDetails
import com.example.cocktail_bar.components.CocktailList
import com.example.cocktail_bar.components.RefreshButton
import com.example.cocktail_bar.components.SendSMSButton
import com.example.cocktail_bar.components.Timer
import com.example.cocktail_bar.ui.theme.CocktailBarTheme

const val cocktailsNum = 16

class MainActivity : ComponentActivity() {
    private val viewModel: CocktailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocktailApp(viewModel)
        }
    }
}

@Composable
fun isTablet(): Boolean {
    val context = LocalContext.current
    return context.resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

@Composable
fun CocktailApp(viewModel: CocktailViewModel) {
    val cocktails = viewModel.cocktails.value
    var selectedCocktail by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState(initial = 0)

    LaunchedEffect(cocktails) {
        if (cocktails.isNotEmpty()) {
            selectedCocktail = 0 // After refreshing the cocktails set index to 0
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    CocktailActivityTemplate (
        snackbarHostState = snackbarHostState,
        scope = scope,
        mainContent = { innerPadding ->
            if (isTablet()){

                Row (modifier = Modifier.padding(innerPadding)) {
                    CocktailList(Modifier.weight(3f), cocktails) { index: Int ->
                        selectedCocktail = index
                    }
                    Box (
                        Modifier
                            .weight(4f)
                            .padding(end = 16.dp)
                            .align(Alignment.CenterVertically)
                            .verticalScroll(scrollState)
                    ) {
                        if (cocktails.isNotEmpty()) {
                            Column {
                                CocktailDetails(cocktails[selectedCocktail])
                                Spacer(modifier = Modifier.size(24.dp))
                                Timer(key = selectedCocktail, minutes = 1, seconds = 0)
                            }

                        }
                        RefreshButton(onClick = {
                            viewModel.fetchRandomCocktails(cocktailsNum)
                        })
                    }
                }

                if (cocktails.isNotEmpty()){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        SendSMSButton(
                            cocktail = cocktails[selectedCocktail],
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            snackbarHostState = snackbarHostState,
                            scope = scope
                        )
                    }
                }
            } else {

                CocktailList(Modifier.padding(innerPadding), cocktails) { index ->
                    selectedCocktail = index
                }
                RefreshButton(
                    modifier = Modifier.padding(innerPadding),
                    onClick = { viewModel.fetchRandomCocktails(cocktailsNum) }
                )
            }
        }
    )
}
