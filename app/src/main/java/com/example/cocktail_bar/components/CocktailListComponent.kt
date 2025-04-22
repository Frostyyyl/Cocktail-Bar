package com.example.cocktail_bar.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cocktail_bar.api.Cocktail
import com.example.cocktail_bar.DetailsActivity
import com.example.cocktail_bar.R
import com.example.cocktail_bar.api.CocktailShort
import com.example.cocktail_bar.utility.isTablet


@Composable
fun CocktailList(modifier: Modifier = Modifier, cocktails: List<CocktailShort>,
                 onCocktailSelected: (Int) -> Unit) {
    LazyColumn(modifier = modifier
        .padding(16.dp)
    ) {
        itemsIndexed(cocktails) { index, cocktail ->
            CocktailItem(index, cocktail, onCocktailSelected)
        }
    }
}


@Composable
fun CocktailItem(index: Int, cocktail: CocktailShort, onCocktailSelected: (Int) -> Unit) {
    val context = LocalContext.current
    val paddingPrimary = 16.dp
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
                        putExtra("id", cocktail.idDrink) // Pass only the ID
                    }
                    context.startActivity(intent)
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cocktail.strDrinkThumb,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp),
            contentDescription = "Cocktail Image",
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
        )

        Column {
            Text(
                text = cocktail.strDrink,
                modifier = Modifier.padding(paddingPrimary / 2),
                fontWeight = FontWeight.Bold
            )
        }
    }
}