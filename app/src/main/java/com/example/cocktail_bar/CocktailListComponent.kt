package com.example.cocktail_bar

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