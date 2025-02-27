package com.rudraksha.rone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Arc
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.Transition
import coil.compose.AsyncImage
import com.rudraksha.rone.model.Article
import com.rudraksha.rone.util.CollapsedImageSize
import com.rudraksha.rone.util.ExpandedImageSize
import com.rudraksha.rone.util.getDefaultGradientColors
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun NewsDescScreen(
    article: Article = Article()
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var showContent by remember { mutableStateOf(false) }
    val progress by remember(scrollState) {
        derivedStateOf {
            (scrollState.value / 400f).coerceIn(0f, 1f)
        }
    }
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
//            .systemBarsPadding()
    ) {
        MotionLayoutHeader(
            progress = progress,
            imageUrl = article.urlToImage,
            name = article.source.name
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                colors = CardColors (
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary,
                ),
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(4.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(4.dp),
                    )
                    article.author?.let {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Author: ",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(4.dp),
                            )
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(4.dp),
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Published: ",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(4.dp),
                        )
                        Text(
                            text = article.publishedAt,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(4.dp),
                        )
                    }
                }
            }

            if (!showContent) {
                article.description?.let {
                    BasicText(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(4.dp),
                        overflow = TextOverflow.Visible
                    )
                }
                if (article.content != null) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                showContent = true
                            }
                        }
                    ) {
                        Text(
                            "Read in detail",
                            maxLines = 1
                        )
                    }
                }
            }
            else {
                article.content?.let {
                    BasicText(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f).padding(4.dp),
                        overflow = TextOverflow.Visible,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MotionLayoutHeader(
    progress: Float,
    imageUrl: String?,
    name: String
) {
    MotionLayout(
        start = ConstraintSet {
            val image = createRefFor("image")
            val title = createRefFor("title")
            constrain(image) {
                width = Dimension.matchParent
                height = Dimension.value(ExpandedImageSize)
                top.linkTo(parent.top, margin = 40.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            constrain(title) {
                top.linkTo(image.bottom, margin = 8.dp)
                start.linkTo(parent.start)
//                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 8.dp)
            }
        },
        end = ConstraintSet {
            val image = createRefFor("image")
            val title = createRefFor("title")
            constrain(image) {
                width = Dimension.value(CollapsedImageSize)
                height = Dimension.value(CollapsedImageSize)
                top.linkTo(parent.top, margin = 4.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 4.dp)
            }
            constrain(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(image.start)
                bottom.linkTo(parent.bottom)
            }
        },
        progress = progress,
        modifier = Modifier
            .wrapContentHeight()
            .background(
                Brush.linearGradient(
                    colors = getDefaultGradientColors()
                )
            ),
        transition = Transition {
            this.motionArc = Arc.StartHorizontal
        }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .layoutId("image")
        )
        Text(
            text = name,
            fontSize = (40 - (10 * progress)).sp,
            color = Color.White,
            modifier = Modifier.layoutId("title"),
        )
    }
}


