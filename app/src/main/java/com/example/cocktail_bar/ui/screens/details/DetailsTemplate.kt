package com.example.cocktail_bar.ui.screens.details

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.cocktail_bar.ui.components.NavigationDrawer
import com.example.cocktail_bar.ui.theme.CocktailBarTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTemplate(
    title: String,
    imageLink: String,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scope: CoroutineScope = rememberCoroutineScope(),
    pagerState: PagerState = rememberPagerState(pageCount = { 3 }),
    onNavigationItemSelected: (Int) -> Unit = {},
    mainContent: @Composable (PaddingValues) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val expandedImageHeight = (screenHeight / 2) - 88.dp
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    CocktailBarTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawer(
                    pagerState = pagerState,
                    scope = scope,
                    onBackClick = { scope.launch { drawerState.close() } },
                    onNavigationItemSelected = onNavigationItemSelected
                )
            },
            gesturesEnabled = true
        ) {
            val containerColor by animateColorAsState(
                targetValue = if (scrollBehavior.state.collapsedFraction < 0.8f) {
                    Color.Transparent
                } else {
                    MaterialTheme.colorScheme.primary.copy(
                        alpha = (scrollBehavior.state.collapsedFraction - 0.8f) / 0.2f
                    )
                },
                animationSpec = tween(300),
                label = "TopBarColorAnimation"
            )

            val imageAlpha by animateFloatAsState(
                targetValue = 1f - (scrollBehavior.state.collapsedFraction * 0.4f),
                animationSpec = tween(300),
                label = "ImageAlphaAnimation"
            )

            val imageHeight by animateDpAsState(
                targetValue = (88.dp + (expandedImageHeight * (1f - scrollBehavior.state.collapsedFraction))),
                animationSpec = tween(durationMillis = 300),
                label = "ImageHeightAnimation"
            )

            val zIndex by animateFloatAsState(
                targetValue = if (scrollBehavior.state.collapsedFraction > 0.8f) {
                    0f
                } else {
                    1f
                }
            )

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    Box {
                        AsyncImage(
                            model = imageLink,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                                .graphicsLayer { alpha = imageAlpha }
                                .zIndex(zIndex)
                        )

                        LargeTopAppBar(
                            title = {
                                if (scrollBehavior.state.collapsedFraction > 0.5f) {
                                    Text(text = title)
                                }
                            },
                            navigationIcon = {
                                if (scrollBehavior.state.collapsedFraction > 0.5f) {
                                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.List,
                                            contentDescription = "Navigation Drawer",
                                            tint = Color.Black
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = containerColor,
                                scrolledContainerColor = MaterialTheme.colorScheme.primary,
                            ),
                            scrollBehavior = scrollBehavior,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                },
                snackbarHost = { SnackbarHost(snackBarHostState) }
            ) { innerPadding ->
                mainContent(innerPadding)
            }
        }
    }
}
