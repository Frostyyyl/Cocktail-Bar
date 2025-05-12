package com.example.cocktail_bar.ui.screens.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.cocktail_bar.ui.components.CocktailDetails
import com.example.cocktail_bar.ui.components.ShareButton
import com.example.cocktail_bar.ui.components.Timer
import com.example.cocktail_bar.ui.theme.CocktailBarTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
class DetailsActivity : ComponentActivity() {
    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val id = intent.getStringExtra("id") ?: ""
            val scrollState = rememberScrollState()

            LaunchedEffect(id) {
                detailsViewModel.fetchCocktailDetails(id)
            }

            val cocktail = detailsViewModel.cocktailState.value
            val title = cocktail?.name ?: "Cocktail Details"
            val imageLink = cocktail?.imageLink ?: ""
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
                            scope = scope,
                            onBackClick = { scope.launch { drawerState.close() } },
                            onNavigationItemSelected = { finish() },
                            onScrollToSection = { section ->
                                when (section) {
                                    "Information" -> {
                                        scope.launch { scrollState.animateScrollTo(0) }
                                    }
                                    "Timer" -> {
                                        scope.launch { scrollState.animateScrollTo(scrollState.maxValue) }
                                    }
                                }
                            }
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
                                            Text(text = title) }
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
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        snackbarHost = { SnackbarHost(snackBarHostState) }
                    ) { innerPadding ->
                        if (detailsViewModel.isLoading.value) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Loading details...")
                            }
                        } else {
                            cocktail?.let {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .verticalScroll(scrollState)
                                    ) {
                                        Spacer(modifier = Modifier.size(16.dp))
                                        CocktailDetails(cocktail = it)
                                        Spacer(modifier = Modifier.size(24.dp))
                                        Timer(minutes = 1, seconds = 0)
                                        Spacer(modifier = Modifier.size(80.dp))
                                    }

                                    ShareButton(
                                        cocktailName = it.name,
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(horizontal = 24.dp, vertical = 12.dp),
                                        snackBarHostState = snackBarHostState,
                                        scope = scope
                                    )
                                }
                            } ?: run {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("An error occurred while loading details.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
