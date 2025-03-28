package com.example.cocktail_bar

import android.R.attr.button
import android.R.attr.onClick
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import coil.compose.AsyncImage
import com.example.cocktail_bar.ui.theme.CocktailBarTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val cocktailsNum = 10

class MainActivity : ComponentActivity() {
    private val viewModel: CocktailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.fetchRandomCocktails(cocktailsNum)
        setContent {
            CocktailApp(viewModel)
        }
    }
}

@Composable
private fun isTablet(): Boolean {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
}

@Composable
fun CocktailApp(viewModel: CocktailViewModel) {
    val cocktails = viewModel.cocktails.value
    CocktailBarTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            if (isTablet()){

                Row (modifier = Modifier.padding(innerPadding)) {
                    CocktailList(Modifier.weight(2f), cocktails)
                    Box(
                        Modifier
                            .weight(3f)
                            .padding(end = 16.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        if (cocktails.isNotEmpty()) {
                            CocktailDetails(cocktails[0])
                        }
                    }
                }
                RefreshButton(onClick = {
                    viewModel.fetchRandomCocktails(cocktailsNum)
                })

            } else {

                CocktailList(Modifier.padding(innerPadding), cocktails)
                RefreshButton(
                    modifier = Modifier.padding(innerPadding),
                    onClick = { viewModel.fetchRandomCocktails(cocktailsNum) }
                )
            }
        }
    }
}

@Composable
fun CocktailList(modifier: Modifier = Modifier, cocktails: List<Cocktail>) {
    val scrollState = rememberScrollState(initial = 0)

    Column(modifier = modifier
        .padding(horizontal = 16.dp)
        .verticalScroll(scrollState)
    ) {
        for (cocktail in cocktails) {
            CocktailItem(cocktail)
        }
    }
}

@Composable
fun CocktailItem(cocktail: Cocktail) {
    val context = LocalContext.current
    val paddingPrimary = 16.dp
    val tagsModifier = Modifier
        .background(MaterialTheme.colorScheme.tertiary)
        .padding(4.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingPrimary)
            .clip(shape = RoundedCornerShape(paddingPrimary))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                val intent = Intent(context, DetailsActivity::class.java).apply {
                    putExtra("cocktail", cocktail)
                }
                context.startActivity(intent)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cocktail.imageLink,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp),
            contentDescription = "Cocktail Image",
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
        )

        Column {
            Text(
                text = cocktail.name,
                modifier = Modifier.padding(paddingPrimary / 2)
            )

            Row(
                modifier = Modifier.padding(paddingPrimary / 2)
            ) {
                Text(
                    text = cocktail.category,
                    modifier = tagsModifier,
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(modifier = Modifier.size(paddingPrimary / 2))

                Text(
                    text = cocktail.alcoholic,
                    modifier = tagsModifier,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun RefreshButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .align(if (isTablet()) Alignment.TopEnd else Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}