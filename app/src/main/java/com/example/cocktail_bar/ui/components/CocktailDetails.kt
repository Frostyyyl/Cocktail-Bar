package com.example.cocktail_bar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cocktail_bar.data.model.Cocktail

@Composable
fun CocktailDetails(cocktail: Cocktail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {

//        Text(
//            text = cocktail.name,
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally),
//            textAlign = TextAlign.Center,
//            style = MaterialTheme.typography.titleLarge,
//        )
//
//        AsyncImage(
//            model = cocktail.imageLink,
//            contentDescription = "Cocktail Image",
//            modifier = Modifier
//                .padding(16.dp)
//                .width(200.dp)
//                .height(200.dp)
//                .align(Alignment.CenterHorizontally),
//            placeholder = painterResource(id = R.drawable.ic_launcher_background),
//        )

        Spacer(modifier = Modifier.size(4.dp))

        val style = SpanStyle(
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.secondary,
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
        isAlcoholic = "No"
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
                append("■ ${measurements[i]}of ${ingredients[i]}\n")
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