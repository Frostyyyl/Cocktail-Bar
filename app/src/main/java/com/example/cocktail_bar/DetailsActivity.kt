package com.example.cocktail_bar

import android.R.attr.category
import android.R.attr.fontWeight
import android.R.attr.text
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Build
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
import androidx.compose.ui.text.TextStyle
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

                    Column(
                        modifier = Modifier.padding(innerPadding).padding(16.dp)
                    ) {
                        ReturnButton()
                        Spacer(modifier = Modifier.size(24.dp))
                        CocktailDetails(
                            cocktail = cocktail
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CocktailDetails(cocktail: Cocktail) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = cocktail.name,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )

        AsyncImage(
            model = cocktail.imageLink,
            contentDescription = "Cocktail Image",
            modifier = Modifier
                .padding(16.dp)
                .width(200.dp)
                .height(200.dp)
                .align(Alignment.CenterHorizontally),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
        )

        Spacer(modifier = Modifier.size(4.dp))

        val style = SpanStyle(
            fontWeight = FontWeight.ExtraBold,
            color = Color.DarkGray,
            fontSize = 20.sp
        )

        CocktailCategory(cocktail.category, style)
        CocktailIsAlcoholic(cocktail.alcoholic, style)
        CocktailIngredients(cocktail.ingredients, cocktail.measurements, style)
        CocktailInstructions(cocktail.instructions, style)
    }
}

@Composable
fun CocktailCategory(category: String, style: SpanStyle) {
    Text(
        buildAnnotatedString {
            withStyle(style = style) {
                append("Category: ")
            }
            append(category)
        }
    )
}

@Composable
fun CocktailIsAlcoholic(alcoholic: String, style: SpanStyle) {
    var isAlcoholic = "Optionally"

    if (alcoholic == "Alcoholic") {
        isAlcoholic = "Yes"
    } else if (alcoholic == "Non Alcoholic") {
        isAlcoholic == "No"
    }

    Text(
        buildAnnotatedString {
            withStyle(style = style) {
                append("Alcoholic: ")
            }
            append(isAlcoholic)
        }
    )
}

@Composable
fun CocktailIngredients(ingredients: List<String>, measurements: List<String>, style: SpanStyle){
    Text(
        buildAnnotatedString {
            withStyle(style = style) {
                append("Ingredients: \n")
            }
            for (i in measurements.indices) {
                append("> ${measurements[i]} of ${ingredients[i]}\n")
            }
        }
    )
}

@Composable
fun CocktailInstructions(instructions: String, style: SpanStyle) {
    Text(
        buildAnnotatedString {
            withStyle(style = style) {
                append("Instructions: ")
            }
            append(instructions.replaceFirstChar { it.uppercase() })
        }
    )
}

@Composable
fun ReturnButton() {
    val context = LocalContext.current

    Button(onClick = { (context as Activity).finish() }
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
        )
    }
}