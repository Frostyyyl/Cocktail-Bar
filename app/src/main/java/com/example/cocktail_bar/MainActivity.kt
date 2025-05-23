package com.example.cocktail_bar

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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

    CocktailBarTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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

            } else {

                CocktailList(Modifier.padding(innerPadding), cocktails) { index ->
                    selectedCocktail = index
                }
                RefreshButton(
                    modifier = Modifier.padding(innerPadding),
                    onClick = { viewModel.fetchRandomCocktails(cocktailsNum)}
                )
            }
        }
    }
}

@Composable
fun CocktailList(modifier: Modifier = Modifier, cocktails: List<Cocktail>,
                 onCocktailSelected: (Int) -> Unit) {
    LazyColumn(modifier = modifier
        .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(cocktails) { index, cocktail ->
            CocktailItem(index, cocktail, onCocktailSelected)
        }
    }
}

@Composable
fun CocktailItem(index: Int, cocktail: Cocktail, onCocktailSelected: (Int) -> Unit) {
    val context = LocalContext.current
    val paddingPrimary = 16.dp
    val tagsModifier = Modifier
        .background(MaterialTheme.colorScheme.secondary)
        .padding(4.dp)
    val isTablet = isTablet()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingPrimary)
            .clip(shape = RoundedCornerShape(paddingPrimary))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                if(isTablet) {
                    onCocktailSelected(index)
                }
                else {
                    val intent = Intent(context, DetailsActivity::class.java).apply {
                        putExtra("cocktail", cocktail)
                    }
                    context.startActivity(intent)
                }
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
                modifier = Modifier.padding(paddingPrimary / 2),
                fontWeight = FontWeight.Bold
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