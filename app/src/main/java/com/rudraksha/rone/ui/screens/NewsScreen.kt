package com.rudraksha.rone.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.rudraksha.rone.model.Article
import com.rudraksha.rone.util.categoryList
import com.rudraksha.rone.util.categoryGradientColors
import com.rudraksha.rone.viewmodel.NewsUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

fun Modifier.categoryWidth(category: String): Modifier {
    return this.width((category.length * 10).dp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    fetchNews: (category: String) -> Unit = {},
    newsUiState: NewsUiState = NewsUiState(),
    onItemClick: (String) -> Unit = {}
) {
    var category by remember { mutableStateOf("general") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(category) {
        coroutineScope.launch {
            Log.d("news[category]", newsUiState.news[category]?.size.toString())
            if (newsUiState.news[category]?.isEmpty() == true || newsUiState.news[category] == null) {
                fetchNews(category)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "News",
                        style = MaterialTheme.typography.headlineLarge,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFF1919),
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        if (newsUiState.errorMessage != null) {
            Dialog(
                onDismissRequest = { fetchNews(category) },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                ),
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.surface)
                )
                Text(newsUiState.errorMessage ?: "Unknown error")
            }
        } else if (newsUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 8.dp
                )
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.fillMaxWidth().height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                ) {
                    categoryList.forEachIndexed { index, item ->
                        val startIndexColor = index % categoryGradientColors.size // Cycle through colors
                        Box(
                            modifier = Modifier
                                .categoryWidth(item)
                                .clip(
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable {
                                    coroutineScope.launch {
                                        category = item
                                    }
                                }
                                .then(
                                    if (category == item) {
                                        Modifier.background(
                                            brush = Brush.linearGradient(
                                                colors = categoryGradientColors.subList(
                                                    startIndexColor,
                                                    categoryGradientColors.size
                                                ) + categoryGradientColors.subList(
                                                    0,
                                                    startIndexColor
                                                )
                                            )
                                        )
                                    }
                                    else {
                                        Modifier.border(
                                            width = 2.dp,
                                            brush = Brush.linearGradient(
                                                colors = categoryGradientColors.subList(
                                                    startIndexColor,
                                                    categoryGradientColors.size
                                                ) + categoryGradientColors.subList(
                                                    0,
                                                    startIndexColor
                                                )
                                            ),
                                            shape = MaterialTheme.shapes.medium
                                        )
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                },
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(newsUiState.news[category] ?: listOf()) { article ->
                        NewsCard(
                            article = article,
                            onItemClick = onItemClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewsCard(
    article: Article,
    onItemClick: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp)
            .clickable {
//                Log.d("OnItemClick", article.url)
                onItemClick(article.url)
            },
        elevation = CardDefaults.cardElevation(4.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .wrapContentSize()
            )
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                maxLines = Int.MAX_VALUE,
                modifier = Modifier
                    .padding(4.dp)
            )
        }
    }
}

@Preview
@Composable
fun NewsPreview() {
    NewsScreen()
}
