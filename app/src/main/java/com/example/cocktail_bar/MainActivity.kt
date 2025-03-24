package com.example.cocktail_bar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cocktail_bar.ui.theme.CocktailBarTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.cocktail_bar.Drink

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocktailBarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CocktailList(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CocktailList(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState(initial = 0)
    val colors = MaterialTheme.colorScheme

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .verticalScroll(scrollState)
        ) {
            for (i in 1..10) {
                val viewModel: CocktailViewModel = remember { CocktailViewModel() }
                LaunchedEffect(Unit) {
                    viewModel.fetchRandomCocktail()
                }
                val cocktail = viewModel.cocktail.value

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(colors.primary),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (cocktail != null) {
                        AsyncImage(
                            model = cocktail.strDrinkThumb,
                            contentDescription = "Cocktail Image",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp),
//                                .clip(AbsoluteRoundedCornerShape(8.dp))
                            placeholder = painterResource(id = R.drawable.ic_launcher_background),
                        )
                        Column {
                            Row(modifier = Modifier.padding(8.dp)) {
                                Text(text = cocktail.strDrink)
                            }

                            Row(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = cocktail.strCategory,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier
                                        .background(colors.tertiary)
                                        .padding(4.dp)
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = cocktail.strAlcoholic,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier
                                        .background(colors.tertiary)
                                        .padding(4.dp)
                                )
                            }
                        }
                    } else {
                        Text("Loading...")
                    }
                }
            }
        }

        // Anchor the button to the bottom of the screen
//        Button(
//            onClick = { viewModel.fetchRandomCocktail() },
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(16.dp) // Optional padding
//        ) {
//            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
//        }
    }
}


