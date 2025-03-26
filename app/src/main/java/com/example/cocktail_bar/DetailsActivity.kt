package com.example.cocktail_bar

import android.R.attr.text
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.cocktail_bar.ui.theme.CocktailBarTheme

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocktailBarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DetailView(
                        modifier = Modifier.padding(innerPadding),
                        drinkName = intent.getStringExtra("drinkName") ?: "Unknown Drink")
                }
            }
        }
    }
}
@Composable
fun DetailView(modifier: Modifier = Modifier, drinkName: String) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    val viewModel: CocktailViewModel = remember { CocktailViewModel() }
    LaunchedEffect(Unit) {
        viewModel.fetchCocktailDetails(drinkName)
    }
    val drinkDetails = viewModel.cocktailDetails.value

    Column (modifier = modifier.padding(16.dp)){
        Button(onClick = {
            context.startActivity(Intent(context, MainActivity::class.java))}
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null,
                modifier = Modifier.scale(scaleX = -1f, scaleY = 1f))
        }
        Spacer(modifier = Modifier.size(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp))
                .background(colors.primary)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "${drinkDetails?.strDrink}",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge,
            )

            AsyncImage(
                model = drinkDetails?.strDrinkThumb,
                contentDescription = "Cocktail Image",
                modifier = Modifier
                    .padding(16.dp)
                    .width(200.dp)
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
            )

            Spacer(modifier = Modifier.size(4.dp))
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.DarkGray,
                        fontSize = 20.sp)) {
                        append("Category:")
                    }
                    append(" ${drinkDetails?.strCategory}")
                }
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold,
                        color = Color.DarkGray,
                        fontSize = 20.sp)) {
                        append("Alcoholic:")
                    }
                    append(" ${drinkDetails?.strAlcoholic}")
                }
            )
            Text(
                text = "Ingredients:",
                fontWeight = FontWeight.ExtraBold,
                color = Color.DarkGray,
                fontSize = 20.sp)
            // For each ingredient and measure
            (1..15).forEach { i ->
                val ingredient = when (i) {
                    1 -> drinkDetails?.strIngredient1
                    2 -> drinkDetails?.strIngredient2
                    3 -> drinkDetails?.strIngredient3
                    4 -> drinkDetails?.strIngredient4
                    5 -> drinkDetails?.strIngredient5
                    6 -> drinkDetails?.strIngredient6
                    7 -> drinkDetails?.strIngredient7
                    8 -> drinkDetails?.strIngredient8
                    9 -> drinkDetails?.strIngredient9
                    10 -> drinkDetails?.strIngredient10
                    11 -> drinkDetails?.strIngredient11
                    12 -> drinkDetails?.strIngredient12
                    13 -> drinkDetails?.strIngredient13
                    14 -> drinkDetails?.strIngredient14
                    15 -> drinkDetails?.strIngredient15
                    else -> null
                } ?: ""

                val measure = when (i) {
                    1 -> drinkDetails?.strMeasure1
                    2 -> drinkDetails?.strMeasure2
                    3 -> drinkDetails?.strMeasure3
                    4 -> drinkDetails?.strMeasure4
                    5 -> drinkDetails?.strMeasure5
                    6 -> drinkDetails?.strMeasure6
                    7 -> drinkDetails?.strMeasure7
                    8 -> drinkDetails?.strMeasure8
                    9 -> drinkDetails?.strMeasure9
                    10 -> drinkDetails?.strMeasure10
                    11 -> drinkDetails?.strMeasure11
                    12 -> drinkDetails?.strMeasure12
                    13 -> drinkDetails?.strMeasure13
                    14 -> drinkDetails?.strMeasure14
                    15 -> drinkDetails?.strMeasure15
                    else -> null
                } ?: ""

                if (ingredient.isNotBlank()) {
                    Text(text = "$measure - $ingredient")
                }
            }
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold,
                        color = Color.DarkGray,
                        fontSize = 20.sp)) {
                        append("Instructions:")
                    }
                    append(" ${drinkDetails?.strInstructions}")
                }
            )
        }
    }
}
